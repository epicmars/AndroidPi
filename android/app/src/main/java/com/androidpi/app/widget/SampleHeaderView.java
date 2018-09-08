package com.androidpi.app.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/8/28.
 */
public class SampleHeaderView extends ConstraintLayout {

    public SampleHeaderView(Context context) {
        this(context, null);
    }

    public SampleHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_sample_header, this);
    }

}
