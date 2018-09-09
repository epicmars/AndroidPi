package com.androidpi.app.viewholder.items;

/**
 * Created by jastrelax on 2018/9/9.
 */
public class SampleUnsplashPhoto {

    private String author;
    private int photoResId;

    public SampleUnsplashPhoto(String author, int photoResId) {
        this.author = author;
        this.photoResId = photoResId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPhotoResId() {
        return photoResId;
    }

    public void setPhotoResId(int photoResId) {
        this.photoResId = photoResId;
    }
}
