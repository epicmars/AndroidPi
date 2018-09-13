package com.androidpi.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.androidpi.app.R;
import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnLoadListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshFooterBehavior;
import com.androidpi.literefresh.widget.LoadingView;
import com.androidpi.literefresh.widget.RefreshFooterLayout;

/**
 * Created by jastrelax on 2018/8/31.
 */
public class FooterLoadingView extends RefreshFooterLayout implements OnScrollListener, OnLoadListener {

    private TextView tvMessage;
    private LoadingView loadingView;

    public FooterLoadingView(Context context) {
        this(context, null);
    }

    public FooterLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_footer_loading, this);
        tvMessage = findViewById(R.id.tv_message);
        loadingView = findViewById(R.id.loading_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RefreshFooterBehavior footerBehavior = LiteRefreshHelper.getAttachedBehavior(this);
        if (footerBehavior != null) {
            footerBehavior.addOnScrollListener(this);
            footerBehavior.addOnLoadListener(this);
        }
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onLoadStart() {
        tvMessage.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReleaseToLoad() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onLoadEnd(@Nullable Throwable throwable) {
        loadingView.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        if (throwable != null) {
            tvMessage.setText(throwable.getMessage());
        } else {
            tvMessage.setText(R.string.loading_complete);
        }
    }
}
