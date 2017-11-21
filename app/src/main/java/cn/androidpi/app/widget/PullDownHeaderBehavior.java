package cn.androidpi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class PullDownHeaderBehavior<V extends View> extends HeaderBehavior<V> implements HeaderBehavior.HeaderListener,
        PullingRefresher {

    private List<PullingListener> mListeners = new ArrayList<>();

    public PullDownHeaderBehavior() {
        this(null, null);
    }

    public PullDownHeaderBehavior(Context context) {
        this(context, null);
    }

    public PullDownHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addHeaderListener(this);
    }

    public void addPullDownListener(PullingListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removePullDownListener(PullingListener listener) {
        if (null == listener)
            return;
        mListeners.remove(listener);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max) {
        for (PullingListener l : mListeners) {
            l.onStartPulling(max);
            l.onRefreshStart();
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max) {
        for (PullingListener l : mListeners) {
            l.onPulling(current, delta, max);
        }
        if (current >= max * 0.9) {
            for (PullingListener l : mListeners) {
                l.onRefreshReady();
            }
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max) {
        for (PullingListener l : mListeners) {
            l.onStopPulling(current, max);
        }
        if (current >= max * 0.9) {
            for (PullingListener l : mListeners) {
                l.onRefresh();
            }
        } else {
            stopScroll(coordinatorLayout, (V)child);
        }
    }

    @Override
    public void refresh() {

    }

    @Override
    public void refreshComplete() {
        for (PullingListener l : mListeners) {
            l.onRefreshComplete();
        }
        stopScroll(getParent(), getChild());
    }

    @Override
    public void refreshTimeout() {
        for (PullingListener l : mListeners) {
            l.onRefreshComplete();
        }
    }

    @Override
    public void refreshCancelled() {
        for (PullingListener l : mListeners) {
            l.onRefreshComplete();
        }
    }

    @Override
    public void refreshException(Exception exception) {
        for (PullingListener l : mListeners) {
            l.onRefreshComplete();
        }
    }
}
