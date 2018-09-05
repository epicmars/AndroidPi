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
public class BehaviorController<B extends AnimationOffsetBehavior>
        implements AnimationOffsetBehavior.ScrollingListener, Loader {

    protected BehaviorController proxy;
    protected B behavior;
    protected List<OnScrollListener> mScrollListeners = new ArrayList<>();
    protected List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    protected List<OnLoadListener> mLoadListeners = new ArrayList<>();

    public BehaviorController(B behavior) {
        this.behavior = behavior;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                              int max, boolean isTouch) {
        for (OnScrollListener l : mScrollListeners) {
            l.onStartScroll(child, max, isTouch);
        }
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                            int current, int max, boolean isTouch) {

    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                         int current, int delta, int max, boolean isTouch) {
        for (OnScrollListener l : mScrollListeners) {
            l.onScroll(child, current, delta, max, isTouch);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                             int current, int max, boolean isTouch) {
        for (OnScrollListener l : mScrollListeners) {
            l.onStopScroll(child, current, max, isTouch);
        }
    }

    @Override
    public void refresh() {
        Timber.d("refresh");
        if (proxy == null) {
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
                    if (proxy != null) {
                        Timber.d("do refresh");
                        copyRemainListeners();
                        proxy.refresh();
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
                if (proxy != null) {
                    proxy.refreshComplete();
                }
            }
        });
    }

    @Override
    public void refreshError(Throwable throwable) {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.refreshError(throwable);
                }
            }
        });
    }

    @Override
    public void load() {
        if (proxy == null) {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    load();
                }
            });
        } else {
            runWithView(() -> {
                if (proxy != null) {
                    copyRemainListeners();
                    proxy.load();
                }
            });
        }
    }

    @Override
    public void loadComplete() {
        runWithView(() -> {
            if (proxy != null) {
                proxy.loadComplete();
            }
        });
    }

    @Override
    public void loadError(Throwable throwable) {
        runWithView(() -> {
            if (proxy != null) {
                proxy.loadError(throwable);
            }
        });
    }

    public BehaviorController getProxy() {
        return proxy;
    }

    public void setProxy(BehaviorController proxy) {
        this.proxy = proxy;
        if (proxy != null) {
            copyRemainListeners();
            behavior.executePendingActions();
        }
    }

    public void copyRemainListeners() {
        Iterator<OnRefreshListener> iterator = mRefreshListeners.iterator();
        while (iterator.hasNext()) {
            proxy.addOnRefreshListener(iterator.next());
            iterator.remove();
        }

        Iterator<OnLoadListener> loadListenerIterator = mLoadListeners.iterator();
        while (loadListenerIterator.hasNext()) {
            proxy.addOnLoadListener(loadListenerIterator.next());
            loadListenerIterator.remove();
        }
    }

    public B getBehavior() {
        return behavior;
    }

    public void setBehavior(B behavior) {
        this.behavior = behavior;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (null == listener)
            return;
        mScrollListeners.add(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        if (null == listener) {
            return;
        }
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.addOnRefreshListener(listener);
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
                if (proxy != null) {
                    proxy.addOnLoadListener(listener);
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
