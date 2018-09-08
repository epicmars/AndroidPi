package com.androidpi.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.androidpi.app.R;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnLoadListener;
import com.androidpi.app.base.widget.literefresh.RefreshFooterBehavior;
import com.androidpi.app.base.widget.literefresh.widgets.LoadingView;
import com.androidpi.app.base.widget.literefresh.widgets.ScrollingHeaderLayout;

/**
 * Created by jastrelax on 2018/8/31.
 */
public class LoadingFooterView extends ScrollingHeaderLayout implements OnLoadListener{

    private TextView tvMessage;
    private LoadingView loadingView;

    public LoadingFooterView(Context context) {
        this(context, null);
    }

    public LoadingFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_loading_footer, this);
        tvMessage = findViewById(R.id.tv_message);
        loadingView = findViewById(R.id.loading_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RefreshFooterBehavior footerBehavior = LiteRefreshHelper.getAttachedBehavior(this);
        if (footerBehavior != null) {
            footerBehavior.addOnLoadListener(this);
        }
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, int initial, int min, int max, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int min, int max, int type) {

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
