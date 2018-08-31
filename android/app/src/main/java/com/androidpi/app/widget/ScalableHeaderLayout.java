package com.androidpi.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.base.widget.literefresh.widgets.ScrollingHeaderLayout;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/8/12.
 */
public class ScalableHeaderLayout extends ScrollingHeaderLayout {

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
    public void onStartScroll(View view, int max, boolean isTouch) {

    }

    @Override
    public void onScroll(View view, int current, int delta, int max, boolean isTouch) {
        int height = getHeight();
        if (current <= height) {
            // Because the view can scroll down and then back. And it will not always reach a position
            // where current equals height so that the scale and translation can be reset.
            // So we need to reset it to original scale and translation, especially when scroll back.
            setScaleX(1f);
            setScaleY(1f);
            setTranslationY(0f);
            return;
        }
        float scale = Math.max(current / (float) height, 1f);
        setScaleX(scale);
        setScaleY(scale);
        setTranslationY(-(scale - 1f) * height / 2f);
    }

    @Override
    public void onStopScroll(View view, int current, int max, boolean isTouch) {

    }
}
