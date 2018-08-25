package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

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

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
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

    private float accumulator = 0;

    private Interpolator scrollDownInterpolator = new LinearInterpolator();
    @Override
    protected float onConsumeOffset(int current, int parentHeight, int offset) {
        float consumed = offset;
        if (current >= 0 && offset > 0) {
            float y = scrollDownInterpolator.getInterpolation(current / (float) parentHeight);
            consumed = (1f - y) * offset;
            if (consumed < 0.5) {
                accumulator += 0.2;
                if (accumulator >= 1) {
                    consumed += 1;
                    accumulator = 0;
                }
            }
        }
        return consumed;
    }
}
