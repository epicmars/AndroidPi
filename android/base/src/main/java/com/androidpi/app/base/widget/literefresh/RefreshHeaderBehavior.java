package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class RefreshHeaderBehavior<V extends View> extends HeaderBehavior<V> implements AnimationOffsetBehavior.ScrollListener,
        Refresher {

    private static final long HOLD_ON_DURATION = 500L;

    private static final int STATE_IDEL = 0;
    private static final int STATE_START = 1;
    private static final int STATE_READY = 2;
    private static final int STATE_REFRESH = 3;
    private static final int STATE_COMPLETE = 4;

    private DecelerateInterpolator downInterpolator = new DecelerateInterpolator(1.5f);
    private List<OnPullListener> mPullListeners = new ArrayList<>();
    private List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    private Handler mHandler;
    private int currentState = STATE_IDEL;

    public RefreshHeaderBehavior(Context context) {
        this(context, null);
    }

    public RefreshHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addScrollListener(this);
    }

    public void addOnPullingListener(OnPullListener listener) {
        if (null == listener)
            return;
        mPullListeners.add(listener);
    }

    public void removeOnPullingListener(OnPullListener listener) {
        if (null == listener)
            return;
        mPullListeners.remove(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        if (null == listener)
            return;
        mRefreshListeners.add(listener);
    }

    public void removeRefreshListener(OnRefreshListener listener) {
        if (null == listener)
            return;
        mRefreshListeners.remove(listener);
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onStartPulling(max, isTouch);
        }
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (current < readyRefreshOffset()) {
            moveToState(STATE_START);
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onPulling(current, delta, max, isTouch);
        }
        if (isTouch) {
            if (current >= readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onStopPulling(current, max);
        }
        if (current >= readyRefreshOffset()) {
            startRefreshing();
        } else {
            stopScroll((V)child, false);
        }
    }

    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams params) {
        super.onAttachedToLayoutParams(params);

    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void startRefreshing() {
        if (!mRefreshListeners.isEmpty()) {
            moveToState(STATE_REFRESH);
            show();
        } else {
            hide();
        }
    }

    private Runnable offsetCallback;

    private void stopScroll(boolean holdOn) {
        stopScroll(getChild(), holdOn);
    }

    protected void stopScroll(@NonNull V child, boolean holdOn) {
        if (isVisible()) {
            if (child.getHandler() == null) return;
            child.getHandler().removeCallbacks(offsetCallback);
            offsetCallback = new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            };
            child.postOnAnimationDelayed(offsetCallback, holdOn ? HOLD_ON_DURATION : 0L);
        }
    }

    @Override
    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        int consumed = offset;
        if (current >= 0 && offset > 0) {
            float y = downInterpolator.getInterpolation(current / (float) parentHeight);
            consumed = (int) ((1f - y) * offset);
        }
        return consumed;
    }

    private int readyRefreshOffset() {
        return getChild().getHeight();
    }

    @Override
    public void refresh() {
        // To avoid unnecessary task enqueueing.
        if (currentState == STATE_REFRESH)
            return;
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (moveToState(STATE_REFRESH)) {
                    show();
                }
            }
        });
    }

    @Override
    public void refreshComplete() {
        refreshCompleted();

    }

    @Override
    public void refreshTimeout() {
        refreshCompleted();
    }

    @Override
    public void refreshCancelled() {
        refreshCompleted();
    }

    @Override
    public void refreshException(Exception exception) {
        refreshCompleted();
    }

    private void refreshCompleted() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                moveToState(STATE_COMPLETE);
            }
        });
    }

    public void runOnUIThread(Runnable runnable) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(runnable);
    }

    private boolean moveToState(int state) {
        switch (currentState) {
            case STATE_IDEL:
                if (state == STATE_START) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefreshStart();
                    }
                    return true;
                } else if (state == STATE_REFRESH) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefresh();
                    }
                    return true;
                }
                return false;
            case STATE_START:
                if (state == STATE_READY) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefreshReady();
                    }
                    return true;
                }
                return false;
            case STATE_READY:
                if (state == STATE_REFRESH) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefresh();
                    }
                    return true;
                } else if (state == STATE_START) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefreshStart();
                    }
                    return true;
                }
                return false;
            case STATE_REFRESH:
                if (state == STATE_COMPLETE) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefreshComplete();
                    }
                    stopScroll(true);
                    moveToState(STATE_IDEL);
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (state == STATE_IDEL) {
                    currentState = state;
                    return true;
                }
                return false;
        }
        return false;
    }

}
