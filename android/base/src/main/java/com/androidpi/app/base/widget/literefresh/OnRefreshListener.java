package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.Nullable;

/**
 * Implementing this interface to listener to the refreshing state of header behavior or content
 * behavior's logical header.
 *
 * Created by jastrelax on 2017/11/29.
 */

public interface OnRefreshListener {

    /**
     * The start of refreshing.
     */
    void onRefreshStart();

    /**
     * The refreshing came to a critical state when user release their finger from screen,
     * which means a touch event or scrolling is over, it will trigger the refreshing to happen.
     */
    void onReleaseToRefresh();

    /**
     * The refreshing is happening now.
     */
    void onRefresh();

    /**
     * The end of refreshing.
     * @param throwable the exception that was reported by a refresher.
     */
    void onRefreshEnd(@Nullable Throwable throwable);
}
