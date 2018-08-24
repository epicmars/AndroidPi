package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View> extends FooterBehavior<V> implements Refresher{

    public RefreshFooterBehavior(Context context) {
        super(context);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnPullingListener(OnPullListener listener) {
        controller.addOnPullingListener(listener);
    }

//    public void removeOnPullingListener(OnPullListener listener) {
//        if (null == listener)
//            return;
//        mPullListeners.remove(listener);
//    }

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
