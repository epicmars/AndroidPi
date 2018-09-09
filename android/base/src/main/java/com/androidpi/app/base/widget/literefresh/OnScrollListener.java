package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Implementing this interface and register it with a behavior, then you can observe the scrolling
 * event of the view that the behavior is attached.
 *
 * Created by jastrelax on 2017/11/16.
 */

public interface OnScrollListener {

    /**
     * The view that a behavior is attached is starting to scroll, when implementing this method,
     * you should be careful with which type of touch event causes the scroll to happen. So does
     * the {@link #onStopScroll(CoordinatorLayout, View, int, int, int, int, int, int)} method of
     * this interface.
     *
     * The reason is that after a normal touch scroll is end, it may be followed by a fling motion
     * immediately which can cause this method be invoked again and start another start-scroll-stop
     * round.
     *
     * @param parent  the view's parent view, it must be CoordinatorLayout
     * @param view    the view that the behavior with which this interface is registered is attached
     * @param initial the initial offset of the view
     * @param trigger the trigger offset of the view related to the refreshing state changing
     * @param min     the minimum offset of the view, the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param max     the maximum offset of the view the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param type    the type of touch event that cause the scrolling to happen
     */
    void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type);

    /**
     * The view that a behavior is attached is scrolling now, you can care less about which type of
     * touch event type now, because no matter what the touch event is, it just scrolls.
     *
     * <strong>
     * Note: When compute a progress percentage, because all the number values are integers, you may
     * need to do some number type conversion to make things right.
     * </strong>
     *
     * @param parent  the view's parent view, it must be CoordinatorLayout
     * @param view    the view that the behavior with which this interface is registered is attached
     * @param current the current offset of the view
     * @param delta   the offset delta
     * @param initial the initial offset of the view
     * @param trigger the trigger offset of the view related to the refreshing state changing
     * @param min     the minimum offset of the view, the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param max     the maximum offset of the view the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param type    the type of touch event that cause the scrolling to happen
     */
    void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type);

    /**
     * The view that a behavior is attached has stopped to scroll, when implementing this method,
     * you should be careful with which type of touch event causes the scrolling.
     *
     * The reason is the same as the {@link #onStartScroll(CoordinatorLayout, View, int, int,
     * int, int, int)} method.
     *
     * @see #onStartScroll(CoordinatorLayout, View, int, int, int, int, int)
     *
     * @param parent  the view's parent view, it must be CoordinatorLayout
     * @param view    the view that the behavior with which this interface is registered is attached
     * @param current the current offset of the view
     * @param initial the initial offset of the view
     * @param trigger the trigger offset of the view related to the refreshing state changing
     * @param min     the minimum offset of the view, the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param max     the maximum offset of the view the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param type    the type of touch event that cause the scrolling to happen
     */
    void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type);
}
