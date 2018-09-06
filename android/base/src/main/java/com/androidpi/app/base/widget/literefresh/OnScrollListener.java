package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface OnScrollListener {

    void onStartScroll(CoordinatorLayout parent, View view, int initial, int min, int max, int type);

    void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int min, int max, int type);

    void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int min, int max, int type);
}
