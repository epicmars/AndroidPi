package com.androidpi.common.libs.readability;


import org.jsoup.nodes.Element;

/**
 * Created by jastrelax on 2018/1/3.
 */

public class LinkElement {

    private Element link;
    private int score;
    private String linkText;
    private String href;


    public LinkElement(Element link, int score, String linkText, String linkHref) {
        this.link = link;
        this.score = score;
        this.linkText = linkText;
        this.href = linkHref;
    }

    public Element getLink() {
        return link;
    }

    public void setLink(Element link) {
        this.link = link;
    }

    public void decrease(int score) {
        this.score -= score;
    }

    public void increase(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
