package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View> extends FooterBehavior<V> implements Refresher, AnimationOffsetBehavior.ScrollListener {

    private List<OnPullListener> mPullListeners = new ArrayList<>();
    private WeakReference<RefreshContentBehavior> refreshContentBehavior;

    public RefreshFooterBehavior(Context context) {
        this(context, null);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addScrollListener(this);
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
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        if (mPullListeners != null) {
            mPullListeners.clear();
        }
        refreshContentBehavior.clear();
    }

    @Override
    public void refresh() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (getRefreshContentBehavior() != null) {
                    getRefreshContentBehavior().refresh();
                }
            }
        });
    }

    @Override
    public void refreshComplete() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (getRefreshContentBehavior() != null) {
                    getRefreshContentBehavior().refreshComplete();
                }
            }
        });

    }

    @Override
    public void refreshError(Exception exception) {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (getRefreshContentBehavior() != null) {
                    getRefreshContentBehavior().refreshComplete();
                }
            }
        });
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
                getRefreshContentBehavior().addOnRefreshListener(listener);
            }
        });
    }

    private RefreshContentBehavior getRefreshContentBehavior() {
        if (refreshContentBehavior == null) {
            refreshContentBehavior = new WeakReference<>((RefreshContentBehavior) getContentBehavior());
        }
        return refreshContentBehavior.get();
    }

}
