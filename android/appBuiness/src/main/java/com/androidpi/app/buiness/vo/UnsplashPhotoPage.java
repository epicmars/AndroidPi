package com.androidpi.app.buiness.vo;

import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2018/8/31.
 */
public class UnsplashPhotoPage {

    public static final int FIRST_PAGE = 0;

    private final int page;
    private final List<ResUnsplashPhoto> photos;

    public UnsplashPhotoPage(int page, List<ResUnsplashPhoto> photos) {
        this.page = page;
        this.photos = photos;
    }

    public boolean isFirstPage() {
        return page == FIRST_PAGE;
    }

    public List<ResUnsplashPhoto> getPhotos() {
        return photos;
    }
}