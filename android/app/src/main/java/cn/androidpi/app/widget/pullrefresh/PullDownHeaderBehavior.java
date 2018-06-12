package cn.androidpi.app.widget.pullrefresh;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class PullDownHeaderBehavior<V extends View> extends HeaderBehavior<V> implements HeaderBehavior.HeaderListener,
        PullingRefresher {

    private List<OnPullingListener> mListeners = new ArrayList<>();
    private List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    private AtomicBoolean isRefreshing = new AtomicBoolean(false);
    private Handler mHandler = new Handler();

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
        if (current >= max * 0.9) {
            if (isRefreshing())
                return;
            for (OnRefreshListener l : mRefreshListeners) {
                l.onRefresh();
            }
            setIsRefreshing(true);
        } else {
            stopScroll(coordinatorLayout, (V)child);
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
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (OnRefreshListener l : mRefreshListeners) {
                    l.onRefresh();
                }
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
                stopScroll(getParent(), getChild());
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
                stopScroll(getParent(), getChild());
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
                stopScroll(getParent(), getChild());
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
                stopScroll(getParent(), getChild());
                setIsRefreshing(false);
            }
        });
    }

    public void runOnUIThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
