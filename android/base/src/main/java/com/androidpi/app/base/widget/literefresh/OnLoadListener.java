package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.Nullable;

/**
 * Implementing this interface to listener to the refreshing state of footer behavior or content
 * behavior's logical footer.
 *
 * Created by jastrelax on 2018/8/24.
 */
public interface OnLoadListener {

    /**
     * The start of loading.
     */
    void onLoadStart();

    /**
     * The loading came to a critical state when user release their finger from screen,
     * which means a touch event or scrolling is over, it will trigger the loading to happen.
     */
    void onReleaseToLoad();

    /**
     * The loading is happening now.
     */
    void onLoad();

    /**
     * The end of loading.
     *
     * @param throwable the exception that was reported by a loader.
     */
    void onLoadEnd(@Nullable Throwable throwable);
}
