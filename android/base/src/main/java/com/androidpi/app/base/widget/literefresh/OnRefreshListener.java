package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.Nullable;

/**
 * Created by jastrelax on 2017/11/29.
 */

public interface OnRefreshListener {

    void onRefreshStart();

    void onReleaseToRefresh();

    void onRefresh();

    void onRefreshEnd(@Nullable Throwable throwable);
}
