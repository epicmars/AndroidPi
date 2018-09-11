package com.androidpi.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/8/30.
 */
public class CircularLoadingView extends AppCompatImageView {

    private CircularProgressDrawable drawable;
    private boolean isStyleReset = true;

    public CircularLoadingView(Context context) {
        this(context, null);
    }

    public CircularLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularLoadingView, 0, 0);
        int color = a.getColor(R.styleable.CircularLoadingView_lr_circle_color, Color.WHITE);
        a.recycle();
        drawable = new CircularProgressDrawable(getContext());
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
        drawable.setColorSchemeColors(color);
        setImageDrawable(drawable);
    }

    public void setProgress(float progress) {
        drawable.setStartEndTrim(progress, 0f);
    }

    public void resetStyle() {
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
        isStyleReset = true;
    }

    public void fillCircle() {
        setProgress(1f);
        drawable.setStrokeWidth(drawable.getStrokeWidth() + drawable.getCenterRadius());
        drawable.setCenterRadius(0.1f);
        isStyleReset = false;
    }

    public void startLoading() {
        if (!isStyleReset) {
            resetStyle();
        }
        drawable.start();
    }

    public void stopLoading() {
        drawable.stop();
        if (!isStyleReset) {
            resetStyle();
        }
        setProgress(1f);
    }

    public void setColor(int color) {
        drawable.setColorSchemeColors(color);
    }

}
