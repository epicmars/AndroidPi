package com.androidpi.base.widget.literefresh;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface OnPullingListener {

    void onStartPulling(int max);

    void onPulling(int current, int delta, int max);

    void onStopPulling(int current, int max);
}
