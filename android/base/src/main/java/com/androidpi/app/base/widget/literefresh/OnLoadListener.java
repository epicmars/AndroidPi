package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.Nullable;

/**
 * Created by jastrelax on 2018/8/24.
 */
public interface OnLoadListener {

    void onLoadStart();

    void onReleaseToLoad();

    void onLoad();

    void onLoadEnd(@Nullable Throwable throwable);
}
