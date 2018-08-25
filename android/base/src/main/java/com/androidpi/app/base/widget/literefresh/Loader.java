package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2018/8/24.
 */
public interface Loader extends RefreshController{

    void load();

    void loadComplete();

    void loadError(Exception exception);
}
