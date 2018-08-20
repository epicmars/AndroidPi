package com.androidpi.app.cweather.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.base.ui.UiUtils;
import com.androidpi.app.base.widget.literefresh.LoadingView;
import com.androidpi.app.base.widget.literefresh.OnPullListener;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;
import com.androidpi.app.cweather.R;

/**
 * Created by jastrelax on 2018/8/18.
 */
public class WeatherHeaderView extends FrameLayout implements OnPullListener, OnRefreshListener{

    private LoadingView loadingView;
    private float offset;

    public WeatherHeaderView(Context context) {
        this(context, null);
    }

    public WeatherHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_weather_header, this);
        loadingView = findViewById(R.id.loading_view);
        offset = UiUtils.getStatusBarHeight(context);
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
            behavior.addOnRefreshListener(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onStartPulling(int max, boolean isTouch) {
        if (isTouch) {
            loadingView.setProgress(0);
        }
    }

    @Override
    public void onPulling(int current, int delta, int max, boolean isTouch) {
        if (!isTouch) return;
        float height = getHeight();
        if (current > offset) {
            float progress = (current - offset) / height;
            loadingView.setProgress(progress);
        }
    }

    @Override
    public void onStopPulling(int current, int max) {

    }

    @Override
    public void onRefreshStart() {

    }

    @Override
    public void onRefreshReady() {

    }

    @Override
    public void onRefresh() {
        loadingView.startLoading();
    }

    @Override
    public void onRefreshComplete(Bundle data) {
        loadingView.finishLoading();
    }
}
