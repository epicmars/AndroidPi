package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2017/11/29.
 */

public interface OnRefreshListener {

    void onRefreshStart();

    void onRefreshReady();

    void onRefresh();

    void onRefreshComplete();
}
