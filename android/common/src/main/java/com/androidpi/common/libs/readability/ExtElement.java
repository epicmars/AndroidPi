package com.androidpi.common.libs.readability;

import org.jsoup.nodes.Element;

/**
 * Created by jastrelax on 2017/12/31.
 */

public class ExtElement {

    private Element element;
    private float contentScore;

    public ExtElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void increaseScore(float increment) {
        contentScore += increment;
    }

    public void decreaseScore(float decrement) {
        contentScore -= decrement;
    }

    public void setContentScore(float contentScore) {
        this.contentScore = contentScore;
    }

    public float getContentScore() {
        return contentScore;
    }
}
