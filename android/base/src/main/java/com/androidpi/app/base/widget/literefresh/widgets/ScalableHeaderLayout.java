package com.androidpi.app.base.widget.literefresh.widgets;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;

/**
 * A simple scalable header layout.
 *
 * Generally you should use this layout as a parent view.
 *
 * If you implement a custom view that extends this view directly
 * and add a {@link com.androidpi.app.base.widget.literefresh.OnScrollListener} to it's
 * attached behavior, the order of the added listeners may be out of order.
 * In that case when you make scale or translation to your extended view, it may be conflict with
 * the view property changes that have been made here.
 *
 * Created by jastrelax on 2018/8/12.
 */
public class ScalableHeaderLayout extends RefreshHeaderLayout implements OnScrollListener {

    public ScalableHeaderLayout(Context context) {
        this(context, null);
    }

    public ScalableHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        behavior.addOnScrollListener(this);
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
        int height = getHeight();
        if (current <= height) {
            // Because the view can scroll down and then back. And it will not always reach a position
            // where current offset equals to height exactly so that the scale and translation can be reset.
            // Therefore we need to reset it to original scale and translation manually, especially when scroll back.
            setScaleX(1f);
            setScaleY(1f);
            setTranslationY(0f);
            return;
        }
        if (current > height) {
            float scale = Math.max(current / (float) height, 1f);
            setScaleX(scale);
            setScaleY(scale);
            setTranslationY(-(scale - 1f) * height / 2f);
        }
    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        RefreshHeaderBehavior behavior = (RefreshHeaderBehavior) params.getBehavior();
        if (behavior != null) {
            // todo : remove scroll listener
        }
    }
}
