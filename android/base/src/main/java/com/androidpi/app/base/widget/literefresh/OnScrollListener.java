package com.androidpi.app.base.widget.literefresh;

import android.view.View;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface OnScrollListener {

    void onStartScroll(View view, int max, boolean isTouch);

    void onScroll(View view, int current, int delta, int max, boolean isTouch);

    void onStopScroll(View view, int current, int max, boolean isTouch);
}
