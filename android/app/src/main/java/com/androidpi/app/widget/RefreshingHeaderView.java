package com.androidpi.app.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidpi.app.R;
import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;
import com.androidpi.literefresh.widget.LoadingView;
import com.androidpi.literefresh.widget.RefreshHeaderLayout;

/**
 * Created by jastrelax on 2017/11/21.
 */

public class RefreshingHeaderView extends RefreshHeaderLayout implements OnScrollListener, OnRefreshListener {

    private TextView tvState;
    private LoadingView loadingView;
    private ImageView ivArrow;
    private ObjectAnimator rotateUpAnimator;
    private ObjectAnimator rotateDownAnimator;
    private int gravity = Gravity.CENTER;

    public RefreshingHeaderView(Context context) {
        this(context, null);
    }

    public RefreshingHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshingHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_scrolling_down_header, this);

        tvState = findViewById(R.id.tv_state);
        ivArrow = findViewById(R.id.iv_arrow);
        ImageViewCompat.setImageTintList(ivArrow, ColorStateList.valueOf(getResources().getColor(R.color.text_gray)));
        loadingView = findViewById(R.id.loading_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RefreshHeaderBehavior behavior = LiteRefreshHelper.getAttachedBehavior(this);
        if (behavior != null) {
            behavior.addOnRefreshListener(this);
            behavior.addOnScrollListener(this);
        }
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
        float height = getHeight();
        if (current <= height) return;
        float scale = Math.max(current / height, 1);
        switch (gravity) {
            case Gravity.CENTER:
                setTranslationY(-(current - height) / 2);
                break;
            case Gravity.TOP:
                setTranslationY(-(current - height));
                break;
            case Gravity.BOTTOM:
            default:
                break;
        }
    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onRefreshStart() {
        loadingView.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        rotateDown();
        tvState.setText("下拉更新");
    }

    @Override
    public void onReleaseToRefresh() {
        loadingView.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        rotateUp();
        tvState.setText("释放以更新");
    }

    @Override
    public void onRefresh() {
        loadingView.setVisibility(VISIBLE);
        ivArrow.setVisibility(GONE);
        tvState.setText("更新中...");
    }

    @Override
    public void onRefreshEnd(Throwable throwable) {
        tvState.setText("更新完成");
    }

    private void rotateUp() {
        if (rotateUpAnimator == null) {
            rotateUpAnimator = ObjectAnimator.ofFloat(ivArrow, "rotation", 0, 180);
            rotateUpAnimator.setDuration(100L);
        }
        rotateUpAnimator.start();
    }

    private void rotateDown() {
        if (rotateDownAnimator == null) {
            rotateDownAnimator = ObjectAnimator.ofFloat(ivArrow, "rotation", 180, 0);
            rotateDownAnimator.setDuration(100L);
        }
        rotateDownAnimator.start();
    }
}
