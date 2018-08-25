package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class RefreshStateMachine implements AnimationOffsetBehavior.ScrollListener, Refresher {

    public interface RefreshStateHandler {

        boolean isValidOffset(int currentOffset);

        int transform(int currentOffset);

        int readyRefreshOffset();

        boolean hasRefreshListeners();

        void onStateChanged(int state);
    }

    static final int STATE_IDLE = 0;
    static final int STATE_START = 1;
    static final int STATE_READY = 2;
    static final int STATE_CANCELLED = 3;
    static final int STATE_CANCELLED_RESET = 4;
    static final int STATE_REFRESH = 5;
    static final int STATE_REFRESH_RESET = 6;
    static final int STATE_COMPLETE = 7;

    protected int currentState = STATE_IDLE;

    private RefreshStateHandler stateHandler;

    public RefreshStateMachine(RefreshStateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        moveToState(STATE_START);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (!stateHandler.isValidOffset(current))
            return;
        if (stateHandler.transform(current) >= stateHandler.readyRefreshOffset()) {
            moveToState(STATE_READY);
        } else {
            moveToState(STATE_START);
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        if (!stateHandler.isValidOffset(current))
            return;
        if (stateHandler.transform(current) >= stateHandler.readyRefreshOffset()) {
            moveToState(STATE_READY);
        } else {
            moveToState(STATE_START);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (!stateHandler.isValidOffset(current)) {
            return;
        }
        if (stateHandler.transform(current) < stateHandler.readyRefreshOffset()) {
            moveToState(STATE_CANCELLED);
            return;
        }
        // If there are refresh listeners and we have reach beyond the trigger point.
        if (!(stateHandler.hasRefreshListeners() && moveToState(STATE_REFRESH))) {
            // If not refresh, then reset.
            if (!moveToState(STATE_REFRESH_RESET)) {
                moveToState(STATE_CANCELLED_RESET);
            }
        }
    }

    /**
     * Try to move to another state.
     *
     * @param state
     * @return
     */
    protected boolean moveToState(int state) {
        switch (state) {
            case STATE_IDLE:
                if (currentState == STATE_COMPLETE
                        || currentState == STATE_CANCELLED
                        || currentState == STATE_REFRESH_RESET
                        || currentState == STATE_CANCELLED_RESET) {
                    currentState = STATE_IDLE;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_START:
                if (currentState == STATE_IDLE || currentState == STATE_READY) {
                    currentState = state;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_CANCELLED:
                if (currentState == STATE_START || currentState == STATE_READY) {
                    currentState = state;
                    onStateChanged(currentState);
                    moveToState(STATE_IDLE);
                    return true;
                }
                return false;
            case STATE_CANCELLED_RESET:
                if (currentState == STATE_START || currentState == STATE_READY) {
                    currentState = state;
                    onStateChanged(currentState);
                    moveToState(STATE_IDLE);
                    return true;
                }
                return false;
            case STATE_READY:
                if (currentState == STATE_START) {
                    currentState = state;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_REFRESH:
                if (currentState == STATE_READY || currentState == STATE_IDLE) {
                    currentState = STATE_REFRESH;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_REFRESH_RESET:
                if (currentState == STATE_REFRESH) {
                    currentState = state;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (currentState == STATE_REFRESH || currentState == STATE_REFRESH_RESET) {
                    currentState = state;
                    onStateChanged(currentState);
                    moveToState(STATE_IDLE);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    void onStateChanged(int state) {
        if (stateHandler == null) return;
        stateHandler.onStateChanged(state);
    }

    boolean isRefreshing() {
        return currentState == STATE_REFRESH;
    }

    @Override
    public void refresh() {
        moveToState(STATE_REFRESH);
    }

    @Override
    public void refreshComplete() {
        refreshCompleted();
    }

    @Override
    public void refreshError(Exception exception) {
        refreshCompleted();
    }

    private void refreshCompleted() {
        moveToState(STATE_COMPLETE);
    }
}
