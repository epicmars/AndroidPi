package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class RefreshHeaderBehavior<V extends View> extends HeaderBehavior<V> implements HeaderBehavior.HeaderListener,
        Refresher {

    private DecelerateInterpolator downInterpolator = new DecelerateInterpolator(1.5f);

    private static final long EXIT_DURATION = 300L;
    private static final long HOLD_ON_DURATION = 500L;
    private static final long REVEAL_DURATION = 500L;

    private List<OnPullingListener> mListeners = new ArrayList<>();
    private List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    private AtomicBoolean isRefreshing = new AtomicBoolean(false);
    private Handler mHandler = new Handler();

    public RefreshHeaderBehavior() {
        this(null, null);
    }

    public RefreshHeaderBehavior(Context context) {
        this(context, null);
    }

    public RefreshHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addHeaderListener(this);
    }

    public void addOnPullingListener(OnPullingListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removeOnPullingListener(OnPullingListener listener) {
        if (null == listener)
            return;
        mListeners.remove(listener);
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
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max) {
        for (OnPullingListener l : mListeners) {
            l.onStartPulling(max);
        }
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshStart();
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max) {
        for (OnPullingListener l : mListeners) {
            l.onPulling(current, delta, max);
        }
        if (current >= max * 0.9) {
            for (OnRefreshListener l : mRefreshListeners) {
                l.onRefreshReady();
            }
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max) {
        for (OnPullingListener l : mListeners) {
            l.onStopPulling(current, max);
        }
        if (current >= max) {
            if (isRefreshing())
                return;
            for (OnRefreshListener l : mRefreshListeners) {
                l.onRefresh();
            }
            hide();
            setIsRefreshing(true);
        } else {
            stopScroll(coordinatorLayout, (V)child, false);
        }
    }

    private Runnable offsetCallback;
    protected void stopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, boolean holdOn) {
        if (isVisible()) {
            if (holdOn) {
                child.getHandler().removeCallbacks(offsetCallback);
                offsetCallback = new Runnable() {
                    @Override
                    public void run() {
                        resetOffsetWithDuration(coordinatorLayout, child, EXIT_DURATION);
                    }
                };
                child.postDelayed(offsetCallback, HOLD_ON_DURATION);
            } else {
                resetOffsetWithDuration(coordinatorLayout, child, EXIT_DURATION);
            }
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

    protected void reveal() {
        if (!isVisible()) {
            if (getChild() == null) return;
            animateOffsetWithDuration(getTopAndBottomOffset() + getChild().getHeight(), REVEAL_DURATION);
        }
    }

    protected void hide() {
        if (isVisible()) {
            int offset = - getChild().getTop();
            animateOffsetWithDuration(getTopAndBottomOffset() + offset, EXIT_DURATION);
        }
    }

    private void resetOffsetWithDuration(CoordinatorLayout coordinatorLayout, final V child, long duration) {
        int offset = - (child.getHeight() + child.getTop());
        if (offset > 0) return;
        animateOffsetWithDuration(getTopAndBottomOffset() + offset, duration);
    }

    public boolean isRefreshing() {
        return isRefreshing.get();
    }

    private void setIsRefreshing(boolean isRefreshing) {
        this.isRefreshing.set(isRefreshing);
    }

    @Override
    public void refresh() {
        // To avoid unnecessary task enqueueing.
        if (isRefreshing())
            return;
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefresh();
                }
                reveal();
                setIsRefreshing(true);
            }
        });
    }

    @Override
    public void refreshComplete() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefreshComplete();
                }
                stopScroll(getParent(), getChild(), true);
                setIsRefreshing(false);
            }
        });
    }

    @Override
    public void refreshTimeout() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefreshComplete();
                }
                stopScroll(getParent(), getChild(), true);
                setIsRefreshing(false);
            }
        });
    }

    @Override
    public void refreshCancelled() {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefreshComplete();
                }
                stopScroll(getParent(), getChild(), true);
                setIsRefreshing(false);
            }
        });
    }

    @Override
    public void refreshException(Exception exception) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefreshComplete();
                }
                stopScroll(getParent(), getChild(), true);
                setIsRefreshing(false);
            }
        });
    }

    public void runOnUIThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
