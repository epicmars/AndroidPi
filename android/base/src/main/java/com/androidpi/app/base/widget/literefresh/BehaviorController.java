package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class BehaviorController<T extends AnimationOffsetBehavior> implements AnimationOffsetBehavior.ScrollListener, Loader {

    protected BehaviorController delegate;
    protected T behavior;
    protected List<OnPullListener> mPullListeners = new ArrayList<>();
    protected List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    protected List<OnLoadListener> mLoadListeners = new ArrayList<>();

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
        Timber.d("refresh");
        if (delegate == null) {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    if (delegate != null) {
                        Timber.d("do refresh");
                        copyRemainListeners();
                        delegate.refresh();
                    }
                }
            });
        }
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

    @Override
    public void load() {
        if (delegate == null) {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    load();
                }
            });
        } else {
            runWithView(() -> {
                if (delegate != null) {
                    copyRemainListeners();
                    delegate.load();
                }
            });
        }
    }

    @Override
    public void loadComplete() {
        runWithView(() -> {
            if (delegate != null) {
                delegate.loadComplete();
            }
        });
    }

    @Override
    public void loadError(Exception exception) {
        runWithView(() -> {
            if (delegate != null) {
                delegate.loadError(exception);
            }
        });
    }

    public BehaviorController getDelegate() {
        return delegate;
    }

    public void setDelegate(BehaviorController delegate) {
        this.delegate = delegate;
        if (delegate != null) {
            copyRemainListeners();
            behavior.executePendingActions();
        }
    }

    public void copyRemainListeners() {
        Iterator<OnRefreshListener> iterator = mRefreshListeners.iterator();
        while (iterator.hasNext()) {
            delegate.addOnRefreshListener(iterator.next());
            iterator.remove();
        }

        Iterator<OnLoadListener> loadListenerIterator = mLoadListeners.iterator();
        while (loadListenerIterator.hasNext()) {
            delegate.addOnLoadListener(loadListenerIterator.next());
            loadListenerIterator.remove();
        }
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

    public void addOnLoadListener(OnLoadListener listener) {
        if (null == listener) {
            return;
        }
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (delegate != null) {
                    delegate.addOnLoadListener(listener);
                } else {
                    mLoadListeners.add(listener);
                }
            }
        });
    }

    protected void runWithView(Runnable runnable) {
        if (runnable == null) return;
        behavior.runWithView(runnable);
    }

    protected void runOnUiThread(Runnable runnable) {
        if (runnable == null) return;
        behavior.runOnUiThread(runnable);
    }
}
