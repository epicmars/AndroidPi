package com.androidpi.app.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidpi.app.R;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;
import com.androidpi.app.base.widget.literefresh.widgets.LoadingView;
import com.androidpi.app.base.widget.literefresh.widgets.RefreshHeaderLayout;

/**
 * Created by jastrelax on 2017/11/21.
 */

public class PullDownHeaderView extends RefreshHeaderLayout implements OnRefreshListener{

    private TextView mTvState;
    private LoadingView loadingView;
    private ImageView ivArrow;
    private ObjectAnimator rotateUpAnimator;
    private ObjectAnimator rotateDownAnimator;
    private int gravity = Gravity.CENTER;

    public PullDownHeaderView(Context context) {
        this(context, null);
    }

    public PullDownHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullDownHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.pull_down_header, this);

        mTvState = findViewById(R.id.tv_state);
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
        }
    }

    @Override
    public void onStartPulling(int max, boolean isTouch) {

    }

    @Override
    public void onPulling(int current, int delta, int max, boolean isTouch) {
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
    public void onStopPulling(int current, int max) {

    }

    @Override
    public void onRefreshStart() {
        loadingView.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        rotateDown();
        mTvState.setText("下拉更新");
    }

    @Override
    public void onReleaseToRefresh() {
        rotateUp();
        mTvState.setText("释放以更新");
    }

    @Override
    public void onRefresh() {
        loadingView.setVisibility(VISIBLE);
        ivArrow.setVisibility(GONE);
        mTvState.setText("更新中...");
    }

    @Override
    public void onRefreshEnd() {
        mTvState.setText("更新完成");
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
