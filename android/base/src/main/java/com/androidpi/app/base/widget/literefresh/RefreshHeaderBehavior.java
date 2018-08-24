package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class RefreshHeaderBehavior<V extends View> extends HeaderBehavior<V> implements Refresher{

    public RefreshHeaderBehavior(Context context) {
        super(context);
    }

    public RefreshHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnPullingListener(OnPullListener listener) {
        controller.addOnPullingListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    @Override
    public void refreshComplete() {
        controller.refreshComplete();
    }

    @Override
    public void refreshError(Exception exception) {
        controller.refreshError(exception);
    }
}
