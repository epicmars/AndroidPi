package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface OnPullListener {

    void onStartPulling(int max, boolean isTouch);

    void onPulling(int current, int delta, int max, boolean isTouch);

    void onStopPulling(int current, int max);
}
