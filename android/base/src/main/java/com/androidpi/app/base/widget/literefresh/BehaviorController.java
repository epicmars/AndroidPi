package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2018/8/24.
 */
public abstract class BehaviorController<T extends AnimationOffsetBehavior> implements AnimationOffsetBehavior.ScrollListener, Refresher {

    protected BehaviorController delegate;
    protected T behavior;
    protected List<OnPullListener> mPullListeners = new ArrayList<>();
    protected List<OnRefreshListener> mRefreshListeners = new ArrayList<>();

    public BehaviorController(T behavior) {
        this.behavior = behavior;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onStartPulling(max, isTouch);
        }
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {

    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onPulling(current, delta, max, isTouch);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onStopPulling(current, max);
        }
    }

    @Override
    public void refresh() {
        behavior.runWithView(new Runnable() {
            @Override
            public void run() {
                if (delegate != null) {
                    delegate.refresh();
                }
            }
        });
    }

    @Override
    public void refreshComplete() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (delegate != null) {
                    delegate.refreshComplete();
                }
            }
        });
    }

    @Override
    public void refreshError(Exception exception) {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (delegate != null) {
                    delegate.refreshError(exception);
                }
            }
        });
    }

    public BehaviorController getDelegate() {
        return delegate;
    }

    public void setDelegate(BehaviorController delegate) {
        this.delegate = delegate;
    }

    public T getBehavior() {
        return behavior;
    }

    public void setBehavior(T behavior) {
        this.behavior = behavior;
    }

    public void addOnPullingListener(OnPullListener listener) {
        if (null == listener)
            return;
        mPullListeners.add(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        if (null == listener) {
            return;
        }
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (delegate != null) {
                    delegate.addOnRefreshListener(listener);
                } else {
                    mRefreshListeners.add(listener);
                }
            }
        });
    }

    protected void runWithView(Runnable runnable) {
        if (runnable == null) return;
        behavior.runWithView(runnable);
    }
}
