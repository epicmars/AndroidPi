package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface OnScrollListener {

    void onStartScroll(CoordinatorLayout parent, View view, int initial, int min, int max, int type);

    /**
     * <strong>
     * Note: When compute a progress percentage, because all the number values are integers, you may
     * need to do some number type conversion to make things right.
     * </strong>
     * @param parent
     * @param view
     * @param current
     * @param delta
     * @param initial
     * @param trigger
     * @param min
     * @param max
     * @param type
     */
    void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type);

    void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int min, int max, int type);
}
