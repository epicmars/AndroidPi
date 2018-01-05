package cn.androidpi.common.libs.readability;

import java.util.regex.Pattern;

/**
 * Created by jastrelax on 2017/12/31.
 */

public class Configuration {

    public interface Regexps {
        /**
         * unlikelyCandidates:    /combx|comment|community|disqus|extra|foot|header|menu|remark|rss|shoutbox|sidebar|sponsor|ad-break|agegate|pagination|pager|popup|tweet|twitter/i,
         * okMaybeItsACandidate:  /and|article|body|column|main|shadow/i,
         * positive:              /article|body|content|entry|hentry|main|page|pagination|post|text|blog|story/i,
         * negative:              /combx|comment|com-|contact|foot|footer|footnote|masthead|media|meta|outbrain|promo|related|scroll|shoutbox|sidebar|sponsor|shopping|tags|tool|widget/i,
         * extraneous:            /print|archive|comment|discuss|e[\-]?mail|share|reply|all|login|sign|single/i,
         * divToPElements:        /<(a|blockquote|dl|div|img|ol|p|pre|table|ul)/i,
         * replaceBrs:            /(<br[^>]*>[ \n\r\t]*){2,}/gi,
         * replaceFonts:          /<(\/?)font[^>]*>/gi,
         * trim:                  /^\s+|\s+$/g,
         * normalize:             /\s{2,}/g,
         * killBreaks:            /(<br\s*\/?>(\s|&nbsp;?)*){1,}/g,
         * videos:                /http:\/\/(www\.)?(youtube|vimeo)\.com/i,
         * skipFootnoteLink:      /^\s*(\[?[a-z0-9]{1,2}\]?|^|edit|citation needed)\s*$/i,
         * nextLink:              /(next|weiter|continue|>([^\|]|$)|Â»([^\|]|$))/i, // Match: next, continue, >, >>, Â» but not >|, Â»| as those usually mean last.
         * prevLink:              /(prev|earl|old|new|<|Â«)/i
         */

        Pattern unlikelyCandidates = Pattern.compile("combx|comment|community|disqus|extra|foot|header|menu|remark|rss|shoutbox|sidebar|sponsor|ad-break|agegate|pagination|pager|popup|tweet|twitter", Pattern.CASE_INSENSITIVE);
        Pattern okMaybeItsACandidate = Pattern.compile("and|article|body|column|main|shadow", Pattern.CASE_INSENSITIVE);
        Pattern positive = Pattern.compile("article|body|content|entry|hentry|main|page|pagination|post|text|blog|story", Pattern.CASE_INSENSITIVE);
        Pattern negative = Pattern.compile("combx|comment|com-|contact|foot|footer|footnote|masthead|media|meta|outbrain|promo|related|scroll|shoutbox|sidebar|sponsor|shopping|tags|tool|widget", Pattern.CASE_INSENSITIVE);
        Pattern extraneous = Pattern.compile("print|archive|comment|discuss|e[\\-]?mail|share|reply|all|login|sign|single", Pattern.CASE_INSENSITIVE);
        Pattern divToPElements = Pattern.compile("<(a|blockquote|dl|div|img|ol|p|pre|table|ul)", Pattern.CASE_INSENSITIVE);
        Pattern replaceBrs = Pattern.compile("(<br[^>]*>[ \n\r\t]*){2,}", Pattern.CASE_INSENSITIVE);
        Pattern replaceFonts = Pattern.compile("<(/?)font[^>]*>", Pattern.CASE_INSENSITIVE);
        Pattern trim = Pattern.compile("^\\s+|\\s+$/g");
        Pattern normalize = Pattern.compile("\\s{2,}/g");
        Pattern killBreaks = Pattern.compile("(<br\\s*/?>(\\s|&nbsp;?)*){1,}");
        Pattern videos = Pattern.compile("http://(www\\.)?(youtube|vimeo)\\.com", Pattern.CASE_INSENSITIVE);
        Pattern skipFootnoteLink = Pattern.compile("^\\s*(\\[?[a-z0-9]{1,2}\\]?|^|edit|citation needed)\\s*$", Pattern.CASE_INSENSITIVE);
        Pattern nextLink = Pattern.compile("(next|weiter|continue|>([^|]|$)|Â»([^|]|$))", Pattern.CASE_INSENSITIVE);
        Pattern prevLink = Pattern.compile("(prev|earl|old|new|<|Â«)", Pattern.CASE_INSENSITIVE);
    }


    public interface Flags {
        int FLAG_STRIP_UNLIKELYS = 0x1;
        int FLAG_WEIGHT_CLASSES = 0x2;
        int FLAG_CLEAN_CONDITIONALLY = 0x4;
    }

}
