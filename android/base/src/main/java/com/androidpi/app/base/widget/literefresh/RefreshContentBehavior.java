package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class RefreshContentBehavior<V extends View> extends ScrollingContentBehavior<V>
        implements Refresher, Loader {

    public RefreshContentBehavior(Context context) {
        this(context, null);
    }

    public RefreshContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
    }

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
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
    public void refreshError(Throwable throwable) {
        controller.refreshError(throwable);
    }

    @Override
    public void load() {
        controller.load();
    }

    @Override
    public void loadComplete() {
        controller.loadComplete();
    }

    @Override
    public void loadError(Throwable throwable) {
        controller.loadError(throwable);
    }

    private float accumulator = 0;

    private Interpolator scrollDownInterpolator = new ViscousFluidInterpolator();

    @Override
    protected float onConsumeOffset(int current, int max, int delta) {
        float consumed = delta;
        if (current >= 0 && delta > 0) {
            float y = scrollDownInterpolator.getInterpolation(current / (float) max);
            consumed = (1f - y) * delta;
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
