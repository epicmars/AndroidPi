package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2018/8/25.
 */
public interface RefreshController {

    void refresh();

    void refreshComplete();

    void refreshError(Exception exception);
}
