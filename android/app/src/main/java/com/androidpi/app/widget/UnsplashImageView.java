package com.androidpi.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by jastrelax on 2018/8/14.
 */
public class UnsplashImageView extends AppCompatImageView {

    public UnsplashImageView(Context context) {
        this(context, null);
    }

    public UnsplashImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnsplashImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        int height = Math.round(getMeasuredWidth() * drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth());
        setMeasuredDimension(getMeasuredWidth(), height);
    }
}
