package cn.androidpi.common.libs.readability;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

import static cn.androidpi.common.libs.readability.Configuration.Flags.FLAG_CLEAN_CONDITIONALLY;
import static cn.androidpi.common.libs.readability.Configuration.Flags.FLAG_STRIP_UNLIKELYS;
import static cn.androidpi.common.libs.readability.Configuration.Flags.FLAG_WEIGHT_CLASSES;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.divToPElements;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.extraneous;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.killBreaks;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.negative;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.nextLink;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.okMaybeItsACandidate;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.positive;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.prevLink;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.replaceBrs;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.replaceFonts;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.unlikelyCandidates;
import static cn.androidpi.common.libs.readability.Configuration.Regexps.videos;

/**
 * Created by jastrelax on 2017/12/31.
 */

public class Readability {

    private URL url;
    private String urlString;
    private Document document;
    private Element body;
    private int curPageNum = 0;
    private List<Element> pageBodies = new ArrayList<>();
    private int FLAGS = FLAG_CLEAN_CONDITIONALLY | FLAG_WEIGHT_CLASSES | FLAG_STRIP_UNLIKELYS;

    /* The maximum number of pages to loop through before we call it quits and just show a link. */
    private static final int MAX_PAGES = 30;
    /* The list of pages we've parsed in this call of readability, for autopaging. As a key store for easier searching. */
    private Map<String, Boolean> parsedPages = new HashMap<>();
    /* A list of the ETag headers of pages we've parsed, in case they happen to match, we'll know it's a duplicate. */
    private List<String> pageETags = new ArrayList<>();

    private boolean fetchMultiplePages = true;

    private String textHtml;

    public Readability(String url) {
        urlString = url;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            Timber.e(e);
        }
    }


    public void init() {
        try {
            document = Jsoup.connect(urlString).get();
            body = document.body();
            textHtml = parse(document, body);
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    /**
     * Runs readability.
     * <p>
     * Workflow:
     * 1. Prep the document by removing script tags, css, etc.
     * 2. Build readability's DOM tree.
     * 3. Grab the article content from the current dom tree.
     * 4. Replace the current DOM tree with the new one.
     * 5. Read peacefully.
     *
     * @param document
     * @param body
     * @return readable html page
     */
    public String parse(Document document, Element body) {
        removeScripts(document);
        /* Make sure this document is added to the list of parsed pages first, so we don't double up on the first page */
        parsedPages.put(document.location().replaceAll("/$", ""), true);
        /* Pull out any possible next page link first */
        String nextPageLink = fetchMultiplePages ? findNextPageLink(document) : null;

        prepDocument();
        /* Build readability's DOM tree */
        Element overlay = document.createElement("DIV");
        Element innerDiv = document.createElement("DIV");
        Element articleTitle = getArticleTitle();
        Element articleContent = grabArticle(null);

        if (articleContent == null) {
            nextPageLink = null;
            return body.html();
        }

        overlay.attr("id", "readOverlay");
        innerDiv.attr("id", "readInner");

        articleTitle.addClass("readability_title");
        document.attr("dir", getSuggestedDirection(articleTitle.html()));

        /* Glue the structure of our document together. */
        innerDiv.appendChild(articleTitle)
                .appendChild(articleContent);
        overlay.appendChild(innerDiv);

        /* Clear the old HTML, insert the new content. */
        body.html("");
        body.attr("id", "readability-content");
        body.appendChild(overlay);
        body.removeAttr("style");

        if (!StringUtil.isBlank(nextPageLink)) {
            appendNextPage(nextPageLink);
        }

        return body.html();
    }

    /**
     * Look for any paging links that may occur within the document.
     *
     * @param element
     */
    public String findNextPageLink(Element element) {
        Map<String, LinkElement> possiblePages = new HashMap<>();
        Elements allLinks = element.getElementsByTag("a");
        String articleBaseUrl = findBaseUrl();

        /**
         * Loop through all links, looking for hints that they may be next-page links.
         * Things like having "page" in their textContent, className or id, or being a child
         * of a node with a page-y className or id.
         *
         * Also possible: levenshtein distance? longest common subsequence?
         *
         * After we do that, assign each page a score, and
         **/
        for (Element link : allLinks) {
            String linkHref = link.attr("href").replaceAll("#.*$", "")
                    .replaceAll("/$", "");
            /* If we've already seen this page, ignore it */
            if (StringUtil.isBlank(linkHref)
                    || linkHref.equalsIgnoreCase(articleBaseUrl)
                    || parsedPages.containsKey(linkHref)) {
                continue;
            }
            /* If it's a absolute path on a different domain, skip it. */
            if (linkHref.contains("://") && !linkHref.contains(url.getHost())) {
                continue;
            }
            /* If the linkText looks like it's not the next page, skip it. */
            String linkText = link.text();
            if (extraneous.matcher(linkText).matches() || linkText.length() > 25) {
                continue;
            }
            /* If the leftovers of the URL after removing the base URL don't contain any digits,
            it's certainly not a next page link. */
            String linkHrefLeftover = linkHref.replace(articleBaseUrl, "");
            if (!linkHrefLeftover.matches("\\d")) {
                continue;
            }

            if (!possiblePages.containsKey(linkHref)) {
                possiblePages.put(linkHref, new LinkElement(link, 0, linkText, linkHref));
            } else {
                LinkElement linkElement = possiblePages.get(linkHref);
                linkElement.setLinkText(linkElement.getLinkText() + "|" + linkText);
            }

            LinkElement linkElement = possiblePages.get(linkHref);
            /**
             * If the articleBaseUrl isn't part of this URL, penalize this link. It could still be the link, but the odds are lower.
             * Example: http://www.actionscript.org/resources/articles/745/1/JavaScript-and-VBScript-Injection-in-ActionScript-3/Page1.html
             **/
            if (linkHref.indexOf(articleBaseUrl) != 0) {
                linkElement.decrease(25);
            }

            String linkData = linkHref + " " + link.className() + " " + link.id();
            if (nextLink.matcher(linkData).matches()) {
                linkElement.increase(50);
            }

            if (linkData.matches("pag(e|ing|inat)")) {
                linkElement.increase(25);
            }

            if (linkData.matches("(first|last)")) {
                // -65 is enough to negate any bonuses gotten from a > or Â» in the text,
                /* If we already matched on "next", last is probably fine. If we didn't, then it's bad. Penalize. */
                if (!nextLink.matcher(linkElement.getLinkText()).matches()) {
                    linkElement.decrease(65);
                }
            }

            if (negative.matcher(linkData).matches()
                    || extraneous.matcher(linkData).matches()) {
                linkElement.decrease(50);
            }

            if (prevLink.matcher(linkData).matches()) {
                linkElement.decrease(200);
            }

            /* If a parentNode contains page or paging or paginat */
            Element parentNode = link.parent();
            boolean positiveNodeMatch = false;
            boolean negativeNodeMatch = false;
            while (parentNode != null) {
                String parentNodeClassAndId = parentNode.className() + " " + parentNode.id();
                if (!StringUtil.isBlank(parentNodeClassAndId)) {
                    if (!positiveNodeMatch
                            && parentNodeClassAndId.matches("pag(e|ing|inat)")) {
                        positiveNodeMatch = true;
                        linkElement.increase(25);
                    }
                    if (!negativeNodeMatch
                            && negative.matcher(parentNodeClassAndId).matches()) {
                        /* If this is just something like "footer", give it a negative.
                        If it's something like "body-and-footer", leave it be. */
                        if (!positive.matcher(parentNodeClassAndId).matches()) {
                            linkElement.decrease(25);
                            negativeNodeMatch = true;
                        }
                    }
                }
                parentNode = parentNode.parent();
            }

            /**
             * If the URL looks like it has paging in it, add to the score.
             * Things like /page/2/, /pagenum/2, ?p=3, ?page=11, ?pagination=34
             **/
            if (linkHref.matches("p(a|g|ag)?(e|ing|ination)?(=|/)[0-9]{1,2}")
                    || linkHref.matches("(page|paging)")) {
                linkElement.increase(25);
            }

            /* If the URL contains negative values, give a slight decrease. */
            if (extraneous.matcher(linkHref).matches()) {
                linkElement.decrease(15);
            }

            /**
             * Minor punishment to anything that doesn't match our current URL.
             * NOTE: I'm finding this to cause more harm than good where something is exactly 50 points.
             *       Dan, can you show me a counterexample where this is necessary?
             * if (linkHref.indexOf(window.location.href) !== 0) {
             *    linkObj.score -= 1;
             * }
             **/

            /**
             * If the link text can be parsed as a number, give it a minor bonus, with a slight
             * bias towards lower numbered pages. This is so that pages that might not have 'next'
             * in their text can still get scored, and sorted properly by score.
             **/
            try {
                int linkTextAsNumber = Integer.parseInt("linkTextAsNumber");
                // Punish 1 since we're either already there, or it's probably before what we want anyways.
                if (linkTextAsNumber == 1) {
                    linkElement.decrease(10);
                } else {
                    linkElement.increase(Math.max(0, 10 - linkTextAsNumber));
                }
            } catch (NumberFormatException ignore) {
            }
        }

        /**
         * Loop thrugh all of our possible pages from above and find our top candidate for the next page URL.
         * Require at least a score of 50, which is a relatively high confidence that this page is the next link.
         **/
        LinkElement topPage = null;
        for (String linkHref : possiblePages.keySet()) {
            LinkElement link = possiblePages.get(linkHref);
            if (link.getScore() > 50
                    && (topPage != null || topPage.getScore() < link.getScore())) {
                topPage = link;
            }
        }

        if (topPage != null) {
            String nextHref = topPage.getHref().replace("/$", "");
            parsedPages.put(nextHref, true);
            return nextHref;
        }
        return null;
    }

    /**
     * Find a cleaned up version of the current URL, to use for comparing links for possible next-pageyness.
     *
     * @return
     */
    public String findBaseUrl() {
        String[] pathSegments = url.getPath().split("/");
        List<String> cleanedSegments = new ArrayList<>();
        String possibleType = "";

        for (int i = 0; i < pathSegments.length; i++) {
            String segment = pathSegments[i];
            // Split off and save anything that looks like a file type.
            if (segment.contains(".")) {
                String[] segmentParts = segment.split("\\.");
                possibleType = segmentParts[1];
                /* If the type isn't alpha-only, it's probably not actually a file extension. */
                if (!possibleType.matches("[^a-zA-Z]")) {
                    segment = segmentParts[0];
                }
            }

            /**
             * EW-CMS specific segment replacement. Ugly.
             * Example: http://www.ew.com/ew/article/0,,20313460_20369436,00.html
             **/
            if (segment.contains(",00")) {
                segment = segment.replace(",00", "");
            }

            // If our first or second segment has anything looking like a page number, remove it.
            if (segment.matches("((_|-)?p[a-z]*|(_|-))[0-9]{1,2}$") && (i == 1 || i == 0)) {
                segment = segment.replaceAll("((_|-)?p[a-z]*|(_|-))[0-9]{1,2}$", "");
            }

            boolean del = false;
            /* If this is purely a number, and it's the first or second segment, it's probably a page number. Remove it. */
            if (i < 2 && segment.matches("^\\d{1,2}$")) {
                del = true;
            }

            /* If this is the first segment and it's just "index", remove it. */
            if (i == 0 && segment.equalsIgnoreCase("index")) {
                del = true;
            }

            /* If our first or second segment is smaller than 3 characters, and the first segment was purely alphas, remove it. */
            if (i < 2 && segment.length() < 3 && pathSegments[0].matches("[a-z]")) {
                del = true;
            }

            /* If it's not marked for deletion, push it to cleanedSegments. */
            if (!del) {
                cleanedSegments.add(segment);
            }
        }

        StringBuilder cleanedPath = new StringBuilder();
        for (String s : cleanedSegments) {
            cleanedPath.append("/").append(s);
        }
        return url.getProtocol() + "://" + url.getAuthority() + cleanedPath;
    }

    private boolean ajaxIsRunning = false;

    /**
     * Make an AJAX request for each page and append it to the document.
     *
     * @param nextPageLink
     **/
    public void appendNextPage(String nextPageLink) {
        curPageNum++;
        final Element articlePage = document.createElement("DIV");
        articlePage.attr("id", "readability-page-" + (curPageNum + 1));
        pageBodies.add(curPageNum, articlePage);
        articlePage.attr("class", "page");

        body.appendChild(articlePage);

        if (curPageNum > MAX_PAGES) {
            String nextPageMarkup = "<div style='text-align: center'><a href='" + nextPageLink + "'>View Next Page</a></div>";
            articlePage.append(nextPageMarkup);
            return;
        }

        class NextPageVisitor {

            /**
             * Now that we've built the article page DOM element, get the page content
             * asynchronously and load the cleaned content into the div we created for it.
             **/
            private void getNextPage(String pageUrl, Element currentPage) {
                try {
                    Connection c = Jsoup.connect(pageUrl);
                    Connection.Response r = c.execute();
                    /* First, check to see if we have a matching ETag in headers - if we do, this is a duplicate page. */
                    String eTag = r.header("ETag");
                    if (!StringUtil.isBlank(eTag)) {
                        if (pageETags.contains(eTag)) {
                            articlePage.attr("style", "display: none;");
                            return;
                        } else {
                            pageETags.add(eTag);
                        }
                    }

                    // TODO: this ends up doubling up page numbers on NYTimes articles. Need to generically parse those away.
                    Element page = document.createElement("DIV");
                    /**
                     * Do some preprocessing to our HTML to make it ready for appending.
                     * â€¢ Remove any script tags. Swap and reswap newlines with a unicode character because multiline regex doesn't work in javascript.
                     * â€¢ Turn any noscript tags into divs so that we can parse them. This allows us to find any next page links hidden via javascript.
                     * â€¢ Turn all double br's into p's - was handled by prepDocument in the original view.
                     *   Maybe in the future abstract out prepDocument to work for both the original document and AJAX-added pages.
                     **/

                    Document responseDoc = r.parse();
                    String responseHtml = responseDoc.outerHtml()
                            .replaceAll("\n", "\uffff")
                            .replaceAll("<script.*?>.*?</script>", "")
                            .replaceAll("\n", "\uffff")
                            .replaceAll("<script.*?>.*?</script>", "")
                            .replaceAll("\uffff", "\n")
                            .replaceAll("<(/?)noscript", "<$1div");

                    responseHtml = replaceBrs.matcher(responseHtml).replaceAll("</p><p>");
                    responseHtml = replaceFonts.matcher(responseHtml).replaceAll("<$1span>");

                    page.html(responseHtml);

                    /**
                     * Reset all flags for the next page, as they will search through it and disable as necessary at the end of grabArticle.
                     **/
                    resetFlag();

                    String nextPageLink = findNextPageLink(page);
                    Element content = grabArticle(page);

                    if (content == null) {
                        ajaxIsRunning = false;
                        return;
                    }

                    /**
                     * Anti-duplicate mechanism. Essentially, get the first paragraph of our new page.
                     * Compare it against all of the the previous document's we've gotten. If the previous
                     * document contains exactly the innerHTML of this first paragraph, it's probably a duplicate.
                     **/

                    Elements paragraphs = content.getElementsByTag("p");
                    Element firstP = (paragraphs != null && paragraphs.size() > 0) ? paragraphs.first() : null;
                    if (null != firstP && firstP.html().length() > 100) {
                        for (int i = 0; i < curPageNum; i++) {
                            Element rPage = pageBodies.get(i);
                            if (rPage != null && rPage.html().contains(firstP.html())) {
                                articlePage.attr("style", "display: none;");
                                parsedPages.put(pageUrl, true);
                                ajaxIsRunning = false;
                                return;
                            }
                        }
                    }

                    currentPage.append(content.html());

                    if (!StringUtil.isBlank(nextPageLink)) {
                        appendNextPage(nextPageLink);
                    } else {
                        ajaxIsRunning = false;
                    }
                } catch (IOException e) {
                    ajaxIsRunning = false;
                }
            }
        }

        NextPageVisitor nextPageVisitor = new NextPageVisitor();
        nextPageVisitor.getNextPage(nextPageLink, articlePage);
    }


    /**
     * Removes script tags from the document.
     *
     * @param element
     */
    public void removeScripts(Element element) {
        Elements scripts = element.getElementsByTag("script");
        for (Element s : scripts) {
            String src = s.attr("src");
            if (StringUtil.isBlank(src)
                    || (!src.contains("readability")
                    && !src.contains("typekit"))) {
                s.empty();
                s.removeAttr("src");
                if (s.hasParent()) {
                    s.remove();
                }
            }
        }
    }

    /**
     * Prepare the HTML document for readability to scrape it.
     * This includes things like stripping javascript, CSS, and handling terrible markup.
     */
    public void prepDocument() {
        body.attr("id", "readabilityBody");
        String innerHtml = body.html();
        /* Turn all double br's into p's */
        /* Note, this is pretty costly as far as processing goes. Maybe optimize later. */
        body.html(replaceFonts.matcher(replaceBrs.matcher(innerHtml).replaceAll("</p><p>")).replaceAll("<$1span>"));
    }

    /**
     * Get the article title as an H1.
     */
    public Element getArticleTitle() {
        String curTitle = "";
        String origTitle = "";
        curTitle = origTitle = document.title();

        // If there's a separator in the title, first remove the final part
        if (curTitle.matches(" [|\\-\\\\/>»_] ")) {
            curTitle = Pattern.compile(" (.*)[|\\-\\\\/>»_] .* ").matcher(origTitle).group(1);

            if (curTitle.split(" ").length < 3) {
                curTitle = Pattern.compile("[^|\\-]*[|\\-](.*)/").matcher(origTitle).group(1);
            }
        } else if (curTitle.contains(": ")) {
            curTitle = Pattern.compile(".*:(.*)").matcher(origTitle).group(1);

            if (curTitle.split(" ").length < 3) {
                curTitle = Pattern.compile("[^:]*[:](.*)").matcher(origTitle).group(1);
            }
        } else if (curTitle.length() > 150 || curTitle.length() < 15) {
            Elements hOnes = body.getElementsByTag("h1");
            if (hOnes.size() == 1) {
                curTitle = hOnes.first().text();
            }
        }

        curTitle = curTitle.trim();
        if (curTitle.split(" ").length < 4) {
            curTitle = origTitle;
        }
        Element articleTitle = document.createElement("h1");
        articleTitle.html(curTitle);

        return articleTitle;
    }


    /**
     * Using a variety of metrics (content score, classname, element types), find the content that is
     * most likely to be the stuff a user wants to read. Then return it wrapped up in a div.
     *
     * @param page a document to run upon. Needs to be a full document, complete with body.
     * @return
     */
    public Element grabArticle(Element page) {

        boolean stripUnlikelyCandidates = flagIsActive(FLAG_STRIP_UNLIKELYS);
        boolean isPaging = (page != null);
        page = isPaging ? page : body;

        String pageCacheHtml = page.html();
        Elements allElements = page.getAllElements();

        /**
         * First, node prepping. Trash nodes that look cruddy (like ones with the class name "comment", etc), and turn divs
         * into P tags where they have been used inappropriately (as in, where they contain no other block level elements.)
         *
         * Note: Assignment from index for performance. See http://www.peachpit.com/articles/article.aspx?p=31567&seqNum=5
         * TODO: Shouldn't this be a reverse traversal?
         **/
        Elements nodesToScore = new Elements();
        for (Element node : allElements) {
            /* Remove unlikely candidates */
            if (stripUnlikelyCandidates) {
                String unlikelyMatchString = node.className() + node.id();
                if (unlikelyCandidates.matcher(unlikelyMatchString).matches()
                        && !okMaybeItsACandidate.matcher(unlikelyMatchString).matches()
                        && !node.tagName().equalsIgnoreCase("BODY")) {
                    node.remove();
                    continue;
                }
            }

            String nodeTagName = node.tagName();
            if (nodeTagName.equalsIgnoreCase("P") ||
                    nodeTagName.equalsIgnoreCase("TD") ||
                    nodeTagName.equalsIgnoreCase("PRE")) {
                nodesToScore.add(node);
            }

            /* Turn all divs that don't have children block level elements into p's */
            if (nodeTagName.equalsIgnoreCase("DIV")) {
                if (!divToPElements.matcher(node.html()).matches()) {
                    node.tagName("P");
                    // jsoup doesn't reparent child node references
                    nodesToScore.add(node);
                } else {
                    /* EXPERIMENTAL */
                    for (Node childNode : node.childNodes()) {
                        if (childNode instanceof TextNode) {
                            Element p = document.createElement("P");
                            p.html(((TextNode) childNode).text());
                            p.attr("style", "display: inline;");
                            p.addClass("readability-styled");
                            childNode.replaceWith(p);
                        }
                    }
                }
            }
        }

        /**
         * Loop through all paragraphs, and assign a score to them based on how content-y they look.
         * Then add their score to their parent node.
         *
         * A score is determined by things like number of commas, class names, etc. Maybe eventually link density.
         **/
        Map<Element, ExtElement> candidates = new HashMap<>();
        for (Element node : nodesToScore) {
            Element parentNode = node.parent();
            Element grandParentNode = parentNode == null ? null : parentNode.parent();
            String innerText = node.text();
            if (parentNode == null || StringUtil.isBlank(parentNode.tagName())) {
                continue;
            }
            /* If this paragraph is less than 25 characters, don't even count it. */
            if (innerText.length() < 25) {
                continue;
            }
            /* Initialize readability data for the parent. */
            if (parentNode != null && !candidates.containsKey(parentNode)) {
                candidates.put(parentNode, initializeNode(parentNode));
            }
            if (grandParentNode != null
                    && !candidates.containsKey(grandParentNode)
                    && !StringUtil.isBlank(grandParentNode.tagName())) {
                candidates.put(grandParentNode, initializeNode(grandParentNode));
            }


            int contentScore = 0;
            /* Add a point for the paragraph itself as a base. */
            contentScore += 1;
            /* Add points for any commas within this paragraph */
            contentScore += innerText.split(",").length;
            /* For every 100 characters in this paragraph, add another point. Up to 3 points. */
            contentScore += Math.min(innerText.length() / 100, 3);
            candidates.get(parentNode).increaseScore(contentScore);
            if (grandParentNode != null && candidates.containsKey(grandParentNode)) {
                candidates.get(grandParentNode).increaseScore(contentScore / 2);
            }
        }

        /**
         * After we've calculated scores, loop through all of the possible candidate nodes we found
         * and find the one with the highest score.
         **/
        Element topCandidate = null;
        for (Element nodeKey : candidates.keySet()) {
            ExtElement candidate = candidates.get(nodeKey);
            /**
             * Scale the final candidates score based on link density. Good content should have a
             * relatively small link density (5% or less) and be mostly unaffected by this operation.
             **/
            float contentScore = candidate.getContentScore();
            candidate.setContentScore(contentScore * (1 - getLinkDensity(nodeKey)));

            if (topCandidate == null || candidate.getContentScore() > candidates.get(topCandidate).getContentScore()) {
                topCandidate = nodeKey;
            }
        }

        /**
         * If we still have no top candidate, just use the body as a last resort.
         * We also have to copy the body node so it is something we can modify.
         **/
        if (topCandidate == null || topCandidate.tagName().equalsIgnoreCase("BODY")) {
            topCandidate = document.createElement("DIV");
            topCandidate.html(page.html());
            page.html("");
            page.appendChild(topCandidate);
            candidates.put(topCandidate, initializeNode(topCandidate));
        }

        /**
         * Now that we have the top candidate, look through its siblings for content that might also be related.
         * Things like preambles, content split by ads that we removed, etc.
         **/
        Element articleContent = document.createElement("DIV");
        if (isPaging) {
            articleContent.attr("id", "readability-content");
        }
        float siblingScoreThreshold = Math.max(10, candidates.get(topCandidate).getContentScore() * 0.2f);
        Elements siblingNodes = topCandidate.parent().children();
        for (Element sibling : siblingNodes) {
            boolean append = false;

            /**
             * Fix for odd IE7 Crash where siblingNode does not exist even though this should be a live nodeList.
             * Example of error visible here: http://www.esquire.com/features/honesty0707
             **/
            if (sibling == null)
                continue;

            float contentBonus = 0;
            /* Give a bonus if sibling nodes and top candidates have the example same classname */
            if (sibling.className().equalsIgnoreCase(topCandidate.className()) && !StringUtil.isBlank(topCandidate.className())) {
                contentBonus += candidates.get(topCandidate).getContentScore() * 0.2f;
            }

            if (candidates.containsKey(sibling) && (candidates.get(sibling).getContentScore() + contentBonus) > siblingScoreThreshold) {
                append = true;
            }

            if (sibling.nodeName().equalsIgnoreCase("P")) {
                float linkDensity = getLinkDensity(sibling);
                String nodeContent = sibling.text();
                int nodeLength = nodeContent.length();

                if (nodeLength > 80 && linkDensity < 0.25) {
                    append = true;
                } else if (nodeLength < 80 && linkDensity == 0 && nodeContent.matches("\\.( |$)")) {
                    append = true;
                }
            }

            if (append) {
                Element nodeToAppend = null;
                if (!sibling.nodeName().equalsIgnoreCase("DIV") && !sibling.nodeName().equalsIgnoreCase("P")) {
                    /* We have a node that isn't a common block level element, like a form or td tag. Turn it into a div so it doesn't get filtered out later by accident. */
                    nodeToAppend = document.createElement("DIV");
                    nodeToAppend.attr("id", sibling.id());
                    nodeToAppend.html(sibling.html());
                } else {
                    nodeToAppend = sibling;
                }
                /* To ensure a node does not interfere with readability styles, remove its classnames */
                nodeToAppend.attributes().remove("class");
                /* Append sibling and subtract from our list because it removes the node when you append to another node */
                articleContent.appendChild(nodeToAppend);
            }
        }

        /**
         * So we have all of the content that we need. Now we clean it up for presentation.
         **/
        prepArticle(candidates, articleContent);

        if (curPageNum == 0) {
            StringBuilder html = new StringBuilder();
            html.append("<div id=\"readability-page-1\" class=\"page\">")
                    .append(articleContent.html())
                    .append("</div>");
            articleContent.html(html.toString());
            pageBodies.add(curPageNum, articleContent.child(0));
        }

        /**
         * Now that we've gone through the full algorithm, check to see if we got any meaningful content.
         * If we didn't, we may need to re-run grabArticle with different flags set. This gives us a higher
         * likelihood of finding the content, and the sieve approach gives us a higher likelihood of
         * finding the -right- content.
         **/
        if (articleContent.text().length() < 250) {
            page.html(pageCacheHtml);

            if (flagIsActive(FLAG_STRIP_UNLIKELYS)) {
                removeFlag(FLAG_STRIP_UNLIKELYS);
                return grabArticle(page);
            } else if (flagIsActive(FLAG_WEIGHT_CLASSES)) {
                removeFlag(FLAG_WEIGHT_CLASSES);
                return grabArticle(page);
            } else if (flagIsActive(FLAG_CLEAN_CONDITIONALLY)) {
                removeFlag(FLAG_CLEAN_CONDITIONALLY);
                return grabArticle(page);
            } else {
                return null;
            }
        }

        return articleContent;
    }

    /**
     * Prepare the article node for display. Clean out any inline styles,
     * iframes, forms, strip extraneous <p> tags, etc.
     *
     * @param articleContent
     */
    public void prepArticle(Map<Element, ExtElement> candidates, Element articleContent) {
        cleanStyles(articleContent);
        killBreaks(articleContent);

        /* Clean out junk from the article content */
        cleanConditionally(candidates, articleContent, "form");
        clean(articleContent, "object");
        clean(articleContent, "h1");

        /**
         * If there is only one h2, they are probably using it
         * as a header and not a subheader, so remove it since we already have a header.
         ***/
        if (articleContent.getElementsByTag("h2").size() == 1) {
            clean(articleContent, "h2");
        }
        clean(articleContent, "iframe");
        cleanHeaders(articleContent);

        /* Do these last as the previous stuff may have removed junk that will affect these */
        cleanConditionally(candidates, articleContent, "table");
        cleanConditionally(candidates, articleContent, "ul");
        cleanConditionally(candidates, articleContent, "div");

        /* Remove extra paragraphs */
        Elements articleParagraphs = articleContent.getElementsByTag("p");
        for (Element p : articleParagraphs) {
            int imgCount = p.getElementsByTag("img").size();
            int embedCount = p.getElementsByTag("embed").size();
            int objectCount = p.getElementsByTag("object").size();
            if (imgCount == 0 && embedCount == 0 && objectCount == 0 && StringUtil.isBlank(p.text())) {
                p.remove();
            }
        }
        String html = articleContent.html().replaceAll("<br[^>]*>\\s*<p", "<p");
        articleContent.html(html);
    }

    /**
     * Remove the style attribute on every element and under.
     * TODO: Test if getElementsByTagName(*) is faster.
     *
     * @param element
     */
    public void cleanStyles(Element element) {
        Element e = (element == null) ? document : element;
        if (e == null)
            return;
        // Remove any root styles, if we're able.
        if (!e.className().equalsIgnoreCase("readability-styled")) {
            e.removeAttr("style");
        }
        // Go until there are no more child nodes
        Element cur = e.children().first();
        while (cur != null) {
            if (!cur.className().equalsIgnoreCase("readability-styled")) {
                cur.removeAttr("style");
            }
            cleanStyles(cur);
            cur = cur.nextElementSibling();
        }
    }

    /**
     * Remove extraneous break tags from a node.
     *
     * @param element
     */
    public void killBreaks(Element element) {
        String html = element.html();
        killBreaks.matcher(html).replaceAll("<br />");
        element.html(html);
    }

    /**
     * Clean an element of all tags of type "tag" if they look fishy.
     * "Fishy" is an algorithm based on content length, classnames, link density, number of images & embeds, etc.
     *
     * @param element
     * @param tag
     */
    public void cleanConditionally(Map<Element, ExtElement> candidates, Element element, String tag) {
        if (!flagIsActive(FLAG_CLEAN_CONDITIONALLY)) {
            return;
        }
        Elements tagsList = element.getElementsByTag(tag);
        int curTagsLength = tagsList.size();
        /**
         * Gather counts for other typical elements embedded within.
         * Traverse backwards so we can remove nodes at the same time without effecting the traversal.
         *
         * TODO: Consider taking into account original contentScore here.
         **/
        for (Element node : tagsList) {
            int weight = getClassWeight(node);
            float contentScore = candidates != null && candidates.containsKey(node) ? (candidates.get(node)).getContentScore() : 0;
            if (weight + contentScore < 0) {
                if (node.hasParent()) node.remove();
            } else if (getCharCount(node, ",") < 10) {
                /**
                 * If there are not very many commas, and the number of
                 * non-paragraph elements is more than paragraphs or other ominous signs, remove the element.
                 **/
                int p = node.getElementsByTag("p").size();
                int img = node.getElementsByTag("img").size();
                int li = node.getElementsByTag("li").size() - 100;
                int input = node.getElementsByTag("input").size();

                int embedCount = 0;
                Elements embeds = node.getElementsByTag("embed");
                for (Element embed : embeds) {
                    if (!videos.matcher(embed.attr("src")).matches()) {
                        embedCount += 1;
                    }
                }
                float linkDensity = getLinkDensity(node);
                int contentLength = node.text().length();
                boolean toRemove = false;

                if (img > p) {
                    toRemove = true;
                } else if (li > p && !tag.equalsIgnoreCase("ul") && !tag.equalsIgnoreCase("ol")) {
                    toRemove = true;
                } else if (input > Math.floor(p / 3.0)) {
                    toRemove = true;
                } else if (contentLength < 25 && (img == 0 || img > 2)) {
                    toRemove = true;
                } else if (weight < 25 && linkDensity > 0.2f) {
                    toRemove = true;
                } else if (weight > 25 && linkDensity > 0.5f) {
                    toRemove = true;
                } else if ((embedCount == 1 && contentLength < 75) || embedCount > 1) {
                    toRemove = true;
                }
                if (toRemove) {
                    if (node.hasParent()) node.remove();
                }
            }
        }
    }

    /**
     * Clean a node of all elements of type "tag".
     * (Unless it's a youtube/vimeo video. People love movies.)
     *
     * @param element
     * @param tag
     */
    public void clean(Element element, String tag) {
        Elements targetList = element.getElementsByTag(tag);
        boolean isEmbed = tag.equalsIgnoreCase("object") || tag.equalsIgnoreCase("embed");
        for (Element e : targetList) {
            /* Allow youtube and vimeo videos through as people usually want to see those. */
            if (isEmbed) {
                StringBuilder attributeValues = new StringBuilder();
                for (Attribute a : e.attributes()) {
                    attributeValues.append(a.getValue()).append("|");
                }
                /* First, check the elements attributes to see if any of them contain youtube or vimeo */
                if (videos.matcher(attributeValues.toString()).matches()) {
                    continue;
                }
                /* Then check the elements inside this element for the same. */
                if (videos.matcher(e.html()).matches()) {
                    continue;
                }
            }
            if (e.hasParent()) {
                e.remove();
            }
        }
    }

    /**
     * Clean out spurious headers from an Element. Checks things like classnames and link density.
     *
     * @param element
     */
    public void cleanHeaders(Element element) {
        for (int headerIndex = 1; headerIndex < 3; headerIndex++) {
            Elements headers = element.getElementsByTag("h" + headerIndex);
            for (Element header : headers) {
                if (getClassWeight(header) < 0 || getLinkDensity(header) > 0.33f) {
                    header.remove();
                }
            }
        }
    }

    /**
     * Get the number of times a string s appears in the node e.
     *
     * @param element
     * @param split
     * @return
     */
    public int getCharCount(Element element, String split) {
        String s = split == null ? "," : split;
        return element.text().split(s).length - 1;
    }

    /**
     * Get the density of links as a percentage of the content
     * This is the amount of text that is inside a link divided by the total text in the node.
     *
     * @param e
     * @return
     */
    public float getLinkDensity(Element e) {
        Elements links = e.getElementsByTag("a");
        int textLength = e.text().length();
        int linkLength = 0;
        for (Element link : links) {
            linkLength += link.text().length();
        }
        return (float) linkLength / textLength;
    }


    /**
     * Initialize a node with the readability object. Also checks the
     * className/id for special names to add to its score.
     *
     * @param node
     */
    public ExtElement initializeNode(Element node) {
        ExtElement extNode = new ExtElement(node);
        switch (extNode.getElement().tagName().toUpperCase()) {
            case "DIV":
                extNode.increaseScore(5);
                break;
            case "PRE":
            case "TD":
            case "BLOCKQUOTE":
                extNode.increaseScore(3);
                break;
            case "ADDRESS":
            case "OL":
            case "UL":
            case "DL":
            case "DD":
            case "DT":
            case "LI":
            case "FORM":
                extNode.decreaseScore(3);
                break;
            case "H1":
            case "H2":
            case "H3":
            case "H4":
            case "H5":
            case "H6":
            case "TH":
                extNode.decreaseScore(5);
                break;
        }
        extNode.increaseScore(getClassWeight(extNode.getElement()));
        return extNode;
    }

    /**
     * Get an elements class/id weight. Uses regular expressions to tell if this
     * element looks good or bad.
     *
     * @param e
     * @return class weight number
     */
    public int getClassWeight(Element e) {
        if (!flagIsActive(FLAG_WEIGHT_CLASSES)) {
            return 0;
        }
        int weight = 0;
        /* Look for a special classname */
        String className = e.className();
        if (!StringUtil.isBlank(className)) {
            if (negative.matcher(className).matches()) weight -= 25;
            if (positive.matcher(className).matches()) weight += 25;
        }
        /* Look for a special ID */
        String id = e.id();
        if (!StringUtil.isBlank(id)) {
            if (negative.matcher(className).matches()) weight -= 25;
            if (positive.matcher(className).matches()) weight += 25;
        }
        return weight;
    }

    /**
     * retuns the suggested direction of the string
     *
     * @param textHtml
     * @return "rtl" or "ltr"
     */
    String getSuggestedDirection(final String textHtml) {

        class DirectionJudger {

            private Pattern heb = Pattern.compile("[\\\\u05B0-\\\\u05F4\\\\uFB1D-\\\\uFBF4]");
            private Pattern arb = Pattern.compile("[\\\\u060C-\\\\u06FE\\\\uFB50-\\\\uFEFC]");

            public String sanitizeText() {
                return textHtml.replaceAll("@\\w+", "");
            }

            public int countMatches(Pattern pattern) {
                int count = 0;
                Matcher matcher = pattern.matcher(textHtml);
                while (matcher.find()) {
                    count++;
                }
                return count;
            }

            public boolean isRTL() {
                int countHebrew = countMatches(heb);
                int countArabic = countMatches(arb);
                // if 20% of chars are Hebrew or Arbic then direction is rtl
                return (countHebrew + countArabic) * 100 / textHtml.length() > 20;
            }
        }

        DirectionJudger judger = new DirectionJudger();
        judger.sanitizeText();
        return judger.isRTL() ? "rtl" : "ltr";
    }

    public boolean flagIsActive(int flag) {
        return (FLAGS & flag) > 0;
    }

    public void removeFlag(int flag) {
        FLAGS &= ~flag;
    }

    public void resetFlag() {
        FLAGS = FLAG_CLEAN_CONDITIONALLY | FLAG_WEIGHT_CLASSES | FLAG_STRIP_UNLIKELYS;
    }

    public String getTextHtml() {
        return textHtml;
    }

}
