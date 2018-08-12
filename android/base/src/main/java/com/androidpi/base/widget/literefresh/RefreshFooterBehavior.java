package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View> extends FooterBehavior<V> implements Refresher, AnimationOffsetBehavior.ScrollListener {

    private List<OnPullListener> mListeners = new ArrayList<>();
    private List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    private AtomicBoolean isRefreshing = new AtomicBoolean(false);
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public RefreshFooterBehavior(Context context) {
        this(context, null);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addScrollListener(this);
    }

    public void addOnPullingListener(OnPullListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removeOnPullingListener(OnPullListener listener) {
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
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max) {
        for (OnPullListener l : mListeners) {
            l.onStartPulling(max);
        }
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max) {
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshStart();
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        for (OnPullListener l : mListeners) {
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
        for (OnPullListener l : mListeners) {
            l.onStopPulling(current, max);
        }
        if (current >= max * 0.9) {
            if (isRefreshing())
                return;
            for (OnRefreshListener l : mRefreshListeners) {
                l.onRefresh();
            }
            setIsRefreshing(true);
        } else {
            stopScroll(coordinatorLayout, (V)child, false);
        }
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // What if multiple tasks is enqueued, check duplicate callbacks.
                if (isRefreshing())
                    return;
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefresh();
                }
                setIsRefreshing(true);
            }
        });
    }

    @Override
    public void refreshComplete() {
        runOnUiThread(new Runnable() {
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
        runOnUiThread(new Runnable() {
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
        runOnUiThread(new Runnable() {
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
        runOnUiThread(new Runnable() {
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

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
