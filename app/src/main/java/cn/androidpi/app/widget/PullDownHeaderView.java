package cn.androidpi.app.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.androidpi.app.R;

/**
 * Created by jastrelax on 2017/11/21.
 */

public class PullDownHeaderView extends FrameLayout implements OnPullingListener, OnRefreshListener {

    private ProgressBar mProgressBar;
    private TextView mTvState;

    public PullDownHeaderView(Context context) {
        this(context, null);
    }

    public PullDownHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullDownHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.pull_down_header, this);

        mProgressBar = findViewById(R.id.progress_bar);
        mTvState = findViewById(R.id.tv_state);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        try {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
            PullDownHeaderBehavior behavior = (PullDownHeaderBehavior) params.getBehavior();
            if (behavior == null) {
                behavior = new PullDownHeaderBehavior();
                params.setBehavior(behavior);
            }
            behavior.addOnPullingListener(this);
            behavior.addOnRefreshListener(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onStartPulling(int max) {

    }

    @Override
    public void onPulling(int current, int delta, int max) {

    }

    @Override
    public void onStopPulling(int current, int max) {

    }

    @Override
    public void onRefreshStart() {
        mTvState.setText("下拉更新");
    }

    @Override
    public void onRefreshReady() {
        mTvState.setText("释放以更新");
    }

    @Override
    public void onRefresh() {
        mTvState.setText("更新中...");
    }

    @Override
    public void onRefreshComplete() {
        mTvState.setText("更新完成");
    }
}
