package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.androidpi.app.base.widget.literefresh.RefreshStateMachine.*;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class ContentBehaviorController extends BehaviorController<ScrollingContentBehavior> implements OnRefreshListener, OnLoadListener {

    private static final long HOLD_ON_DURATION = 500L;
    private static final long SHOW_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private final RefreshStateHandler footerStateHandler = new RefreshStateHandler() {
        @Override
        public boolean isValidOffset(int currentOffset) {
            return transform(currentOffset) > behavior.getFooterConfig().getInitialVisibleHeight();
        }

        @Override
        public int transform(int currentOffset) {
            // The current offset here is the content's top and bottom offset.
            return -(currentOffset + behavior.getChild().getHeight()) + behavior.getParent().getHeight();
        }

        @Override
        public int readyRefreshOffset() {
            return behavior.getFooterConfig().getRefreshTriggerRange() + behavior.getFooterConfig().getInitialVisibleHeight();
        }

        @Override
        public boolean hasRefreshListeners() {
            return mLoadListeners != null && !mLoadListeners.isEmpty();
        }

        @Override
        public void onStateChanged(int state, Throwable throwable) {
//            Timber.d("footer state: %d", state);
            switch (state) {
                case STATE_START:
                    onLoadStart();
                    break;
                case STATE_READY:
                    onReleaseToLoad();
                    break;
                case STATE_CANCELLED:
                    stopScroll(false);
                    break;
                case STATE_CANCELLED_RESET:
                    reset();
                    break;
                case STATE_REFRESH_RESET:
                    refreshFooter();
                    break;
                case STATE_REFRESH:
                    onLoad();
                    refreshFooter();
                    break;
                case STATE_COMPLETE:
                    onLoadEnd(throwable);
                    stopScroll(true);
                    break;
                case STATE_IDLE:
                default:
                    break;
            }
        }
    };

    private RefreshStateHandler headerStateHandler = new RefreshStateHandler() {

        @Override
        public boolean isValidOffset(int currentOffset) {
            return transform(currentOffset) > behavior.getHeaderConfig().getInitialVisibleHeight();
        }

        @Override
        public int transform(int currentOffset) {
            // The current offset here is the content's top and bottom offset.
            return currentOffset;
        }

        @Override
        public int readyRefreshOffset() {
            return behavior.getHeaderConfig().getRefreshTriggerRange() + behavior.getHeaderConfig().getInitialVisibleHeight();
        }

        @Override
        public boolean hasRefreshListeners() {
            return mRefreshListeners != null && !mRefreshListeners.isEmpty();
        }

        @Override
        public void onStateChanged(int state, Throwable throwable) {
//            Timber.d("header state: %d", state);
            switch (state) {
                case STATE_START:
                    onRefreshStart();
                    break;
                case STATE_READY:
                    onReleaseToRefresh();
                    break;
                case STATE_CANCELLED:
                    stopScroll(false);
                    break;
                case STATE_CANCELLED_RESET:
                    reset();
                    break;
                case STATE_REFRESH_RESET:
                    refreshHeader();
                    break;
                case STATE_REFRESH:
                    onRefresh();
                    refreshHeader();
                    break;
                case STATE_COMPLETE:
                    stopScroll(true);
                    onRefreshEnd(throwable);
                    break;
                case STATE_IDLE:
                default:
                    break;
            }
        }
    };

    private RefreshStateMachine headerStateMachine = new RefreshStateMachine(headerStateHandler);
    private RefreshStateMachine footerStateMachine = new RefreshStateMachine(footerStateHandler);
    private Set<RefreshStateMachine> stateMachines = new LinkedHashSet<RefreshStateMachine>(2) {
        {
            add(headerStateMachine);
            add(footerStateMachine);
        }
    };

    public ContentBehaviorController(ScrollingContentBehavior behavior) {
        super(behavior);
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onStartScroll(coordinatorLayout, child, max, isTouch);
        }
        super.onStartScroll(coordinatorLayout, child, max, isTouch);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onPreScroll(coordinatorLayout, child, current, max, isTouch);
        }
        super.onPreScroll(coordinatorLayout, child, current, max, isTouch);
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onScroll(coordinatorLayout, child, current, delta, max, isTouch);
        }
        super.onScroll(coordinatorLayout, child, current, delta, max, isTouch);
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onStopScroll(coordinatorLayout, child, current, max, isTouch);
        }
        super.onStopScroll(coordinatorLayout, child, current, max, isTouch);
    }

    @Override
    public void onLoadStart() {
        for (OnLoadListener l : mLoadListeners) {
            l.onLoadStart();
        }
    }

    @Override
    public void onReleaseToLoad() {
        for (OnLoadListener l : mLoadListeners) {
            l.onReleaseToLoad();
        }
    }

    @Override
    public void onLoad() {
        for (OnLoadListener l : mLoadListeners) {
            l.onLoad();
        }
    }

    @Override
    public void onLoadEnd(Throwable throwable) {
        for (OnLoadListener l : mLoadListeners) {
            l.onLoadEnd(throwable);
        }
    }

    @Override
    public void onRefreshStart() {
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshStart();
        }
    }

    @Override
    public void onReleaseToRefresh() {
        for (OnRefreshListener l : mRefreshListeners) {
            l.onReleaseToRefresh();
        }
    }

    @Override
    public void onRefresh() {
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefresh();
        }
    }

    @Override
    public void onRefreshEnd(Throwable throwable) {
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshEnd(throwable);
        }
    }

    @Override
    public void refresh() {
        // Avoid unnecessary task queueing.
        if (headerStateMachine.isRefreshing())
            return;
        runWithView(new Runnable() {
            @Override
            public void run() {
                headerStateMachine.refresh();
            }
        });
    }

    @Override
    public void refreshComplete() {
        headerStateMachine.refreshComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        headerStateMachine.refreshError(throwable);
    }

    @Override
    public void load() {
        // Avoid unnecessary task queueing.
        if (footerStateMachine.isRefreshing())
            return;
        runWithView(new Runnable() {
            @Override
            public void run() {
                footerStateMachine.refresh();
            }
        });
    }

    @Override
    public void loadComplete() {
        footerStateMachine.refreshComplete();
    }

    @Override
    public void loadError(Throwable throwable) {
        footerStateMachine.refreshError(throwable);
    }

    public boolean isRefreshing() {
        return headerStateMachine.isRefreshing();
    }

    public boolean isLoading() {
        return footerStateMachine.isRefreshing();
    }

    void showHeader() {
        behavior.showHeader(SHOW_DURATION);
    }

    void showFooter() {
        behavior.showFooter(SHOW_DURATION);
    }

    void refreshFooter() {
        behavior.refreshFooter(SHOW_DURATION);
    }

    void refreshHeader() {
        behavior.refreshHeader(SHOW_DURATION);
    }

    void reset() {
        behavior.reset(RESET_DURATION);
    }

    void stopScroll(boolean holdOn) {
        behavior.stopScroll(holdOn);
    }
}
