package com.androidpi.app.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.R;
import com.androidpi.app.base.widget.literefresh.OnPullListener;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;

/**
 * Created by jastrelax on 2018/8/12.
 */
public class ScalableHeaderView extends FrameLayout implements OnPullListener {

    public ScalableHeaderView(Context context) {
        this(context, null);
    }

    public ScalableHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.profile_header_view, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
            RefreshHeaderBehavior behavior = (RefreshHeaderBehavior) params.getBehavior();
            if (behavior == null) {
                behavior = new RefreshHeaderBehavior(getContext());
                params.setBehavior(behavior);
            }
            behavior.addOnPullingListener(this);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onStartPulling(int max, boolean isTouch) {

    }

    @Override
    public void onPulling(int current, int delta, int max, boolean isTouch) {
        float height = getHeight();
        float scale = Math.max(current / height, 1);
        setScaleX(scale);
        setScaleY(scale);
        setTranslationY(-(scale - 1f) * height / 2);
    }

    @Override
    public void onStopPulling(int current, int max) {

    }
}
