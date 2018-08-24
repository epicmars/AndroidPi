package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class ContentBehaviorController extends BehaviorController<ContentBehavior> implements OnRefreshListener, OnLoadListener{

    private static final long HOLD_ON_DURATION = 500L;
    private static final long SHOW_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private RefreshStateMachine headerStateMachine = new HeaderRefreshStateMachine(this);
    private RefreshStateMachine footerStateMachine = new FooterRefreshStateMachine(this);
    private Set<RefreshStateMachine> stateMachines = new LinkedHashSet<>();

    {
        stateMachines.add(headerStateMachine);
        stateMachines.add(footerStateMachine);
    }

    public ContentBehaviorController(ContentBehavior behavior) {
        super(behavior);
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        super.onStartScroll(coordinatorLayout, child, max, isTouch);
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onStartScroll(coordinatorLayout, child, max, isTouch);
        }
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        super.onPreScroll(coordinatorLayout, child, current, max, isTouch);
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onPreScroll(coordinatorLayout, child, current, max, isTouch);
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        super.onScroll(coordinatorLayout, child, current, delta, max, isTouch);
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onScroll(coordinatorLayout, child, current, delta, max, isTouch);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        super.onStopScroll(coordinatorLayout, child, current, max, isTouch);
        for (RefreshStateMachine stateMachine : stateMachines) {
            stateMachine.onStopScroll(coordinatorLayout, child, current, max, isTouch);
        }
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
    public void onLoadEnd() {
        for (OnLoadListener l : mLoadListeners) {
            l.onLoadEnd();
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
    public void onRefreshEnd() {
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshEnd();
        }
    }

    @Override
    public void refresh() {
        headerStateMachine.refresh();
    }

    @Override
    public void refreshComplete() {
        headerStateMachine.refreshComplete();
    }

    @Override
    public void refreshError(Exception exception) {
        headerStateMachine.refreshError(exception);
    }

    @Override
    public void load() {
        footerStateMachine.load();
    }

    @Override
    public void loadComplete() {
        footerStateMachine.loadComplete();
    }

    @Override
    public void loadError(Exception exception) {
        footerStateMachine.loadError(exception);
    }

    void showHeader() {
        behavior.showHeader(SHOW_DURATION);
    }

    void showFooter() {
        behavior.showFooter(SHOW_DURATION);
    }

    void reset() {
        behavior.reset(RESET_DURATION);
    }

    void stopScroll(boolean holdOn) {
        behavior.stopScroll(holdOn);
    }

}
