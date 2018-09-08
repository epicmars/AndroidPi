package com.androidpi.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by jastrelax on 2018/8/30.
 */
public class CircularLoadingView extends AppCompatImageView {

    private CircularProgressDrawable drawable;

    public CircularLoadingView(Context context) {
        this(context, null);
    }

    public CircularLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        drawable = new CircularProgressDrawable(getContext());
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
        drawable.setColorSchemeColors(Color.WHITE);
        setImageDrawable(drawable);
    }

    public void setProgress(float progress) {
        drawable.setStartEndTrim(progress, 0f);
    }

    public void resetStyle() {
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
    }

    public void fillCircle() {
        drawable.setStrokeWidth(drawable.getStrokeWidth() + drawable.getCenterRadius());
        drawable.setCenterRadius(0.1f);
    }

    public void startLoading() {
        drawable.start();
    }

    public void stopLoading() {
        drawable.stop();
    }

    public void setColor(int color) {
        drawable.setColorSchemeColors(color);
    }

}
