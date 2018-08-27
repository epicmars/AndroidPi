package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface OnScrollListener {

    void onStartScroll(int max, boolean isTouch);

    void onScroll(int current, int delta, int max, boolean isTouch);

    void onStopScroll(int current, int max);
}
