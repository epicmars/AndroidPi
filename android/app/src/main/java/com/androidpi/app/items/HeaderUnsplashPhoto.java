package com.androidpi.app.items;

import com.androidpi.data.remote.dto.ResUnsplashPhoto;

/**
 * Created by jastrelax on 2018/8/28.
 */
public class HeaderUnsplashPhoto {

    private ResUnsplashPhoto unsplashPhoto;

    public HeaderUnsplashPhoto(ResUnsplashPhoto unsplashPhoto) {
        this.unsplashPhoto = unsplashPhoto;
    }

    public ResUnsplashPhoto getUnsplashPhoto() {
        return unsplashPhoto;
    }

    public void setUnsplashPhoto(ResUnsplashPhoto unsplashPhoto) {
        this.unsplashPhoto = unsplashPhoto;
    }
}
