package com.androidpi.app.base.widget.literefresh;

/**
 * An controller of refreshing behaviors.
 *
 * <p>
 *     If using in multiple threads, the implementations should be thread safe.
 * </p>
 * Created by jastrelax on 2017/11/16.
 */
public interface Refresher extends RefreshController{

    void refresh();

    void refreshComplete();

    void refreshError(Throwable throwable);
}
