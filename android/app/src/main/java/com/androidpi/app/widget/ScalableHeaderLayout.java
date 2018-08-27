package com.androidpi.app.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.androidpi.app.base.widget.literefresh.widgets.RefreshHeaderLayout;

/**
 * Created by jastrelax on 2018/8/12.
 */
public class ScalableHeaderLayout extends RefreshHeaderLayout {

    public ScalableHeaderLayout(Context context) {
        this(context, null);
    }

    public ScalableHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onStartScroll(int max, boolean isTouch) {

    }

    @Override
    public void onScroll(int current, int delta, int max, boolean isTouch) {
        float height = getHeight();
        if (current < height) return;
        float scale = Math.max(current / height, 1);
        setScaleX(scale);
        setScaleY(scale);
        setTranslationY(-(scale - 1f) * height / 2);
    }

    @Override
    public void onStopScroll(int current, int max) {

    }
}
