package com.androidpi.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/9/6.
 */
public class SampleToolbar extends FrameLayout{

    public SampleToolbar(Context context) {
        this(context, null);
    }

    public SampleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.sample_toolbar, this);
    }

}
