package com.androidpi.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.androidpi.common.libs.readability.Readability;

/**
 * Created by jastrelax on 2018/1/4.
 */

@RunWith(JUnit4.class)
public class ReadabilityTest {

    @Test
    public void test() {
        Readability readability = new Readability("http://itech.ifeng.com/44826697/news.shtml");
        readability.init();
        System.out.println("--------------------------------------------------");
        System.out.println("--------------------- body -----------------------");
        System.out.println("--------------------------------------------------");

        System.out.println(readability.getArticleHtml());
        System.out.println("--------------------------------------------------");
        System.out.println("------------------- textHtml ---------------------");
        System.out.println("--------------------------------------------------");

        String testTemplate = "web/static/readability/test/template.html";
        String testHtmlFile = "web/test.html";
        String htmlTemplate = readFile(testTemplate);
        Document document = Jsoup.parse(htmlTemplate);
        Element article = document.getElementById("article");

        Element element = document.createElement("div");
        element.html(readability.getArticleHtml());

        article.replaceWith(element);

        writeFile(testHtmlFile, document.outerHtml());
    }

    public void writeFile(String filename, String input) {
        Writer fw = null;
        try {
            String path = getClass().getClassLoader().getResource(filename).getPath();
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
            fw.write(input);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {}
            }
        }
    }

    public String readFile(String filename) {
        BufferedReader fr = null;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
            String line = null;
            while ((line = fr.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ignore) {}
        }
        return sb.toString();
    }
}
