package com.androidpi.app.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/9/8.
 */
public class SampleAnchoredView extends ConstraintLayout {

    public SampleAnchoredView(Context context) {
        this(context, null);
    }

    public SampleAnchoredView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleAnchoredView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_sample_anchored, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            CoordinatorLayout.LayoutParams params = ((CoordinatorLayout.LayoutParams) getLayoutParams());
            params.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -64, getResources().getDisplayMetrics());
            setLayoutParams(params);
        } catch (ClassCastException e) {

        }
    }
}
