package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class RefreshContentBehavior<V extends View> extends ContentBehavior<V> implements Refresher{

    public RefreshContentBehavior(Context context) {
        super(context);
    }

    public RefreshContentBehavior(Context context, AttributeSet attrs) {
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

//    public void addOnRefreshListeners(Collection<OnRefreshListener> listeners) {
//        if (null == listeners || listeners.isEmpty()) return;
//        mRefreshListeners.addAll(listeners);
//    }
//
//    public void removeRefreshListener(OnRefreshListener listener) {
//        if (null == listener)
//            return;
//        mRefreshListeners.remove(listener);
//    }

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

    private Interpolator downInterpolator = new AccelerateDecelerateInterpolator();
    @Override
    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        int consumed = offset;
        if (current >= 0 && offset > 0) {
            float y = downInterpolator.getInterpolation(current / (float) parentHeight);
            consumed = (int) ((1f - y) * offset);
        }
        return consumed;
    }
}
