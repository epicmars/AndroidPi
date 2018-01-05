package cn.androidpi.common.libs.readability;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by jastrelax on 2018/1/4.
 */

public class ReaderHelper {

    public static String replaceTemplate(String htmlTemplate, String regex, String replacement) {
        return htmlTemplate.replaceAll(regex, replacement);
    }

    public static String replaceTemplateById(String htmlTemplate, String id, String replacement) {
        try {
            Document document = Jsoup.parse(htmlTemplate);
            Element article = document.getElementById(id);

            Element element = document.createElement("div");
            element.html(replacement);
            article.replaceWith(element);
            return document.outerHtml();
        } catch (Exception e) {
            return null;
        }
    }
}
