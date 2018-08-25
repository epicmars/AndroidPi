package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/24.
 */
public abstract class RefreshStateMachine implements AnimationOffsetBehavior.ScrollListener, Refresher, Loader {



    static final int STATE_IDEL = 0;
    static final int STATE_START = 1;
    static final int STATE_READY = 2;
    static final int STATE_REFRESH = 3;
    static final int STATE_COMPLETE = 4;

    protected int currentState = STATE_IDEL;

    protected ContentBehaviorController controller;

    public RefreshStateMachine(ContentBehaviorController controller) {
        this.controller = controller;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        moveToState(STATE_START);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (!isValidOffset(current))
            return;
        if (isTouch) {
            if (transform(current) >= readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        if (!isValidOffset(current))
            return;
        if (isTouch) {
            if (transform(current) >= readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (!isValidOffset(current)) {
            moveToState(STATE_IDEL);
            return;
        }
        if (transform(current) >= readyRefreshOffset()) {
            startRefreshing();
        } else {
            controller.stopScroll(false);
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
            case STATE_IDEL:
                if (currentState == STATE_COMPLETE) {
                    currentState = STATE_IDEL;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_START:
                if (currentState == STATE_IDEL || currentState == STATE_READY) {
                    currentState = state;
                    onStateChanged(currentState);
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
                if (currentState != STATE_REFRESH) {
                    currentState = STATE_REFRESH;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (currentState == STATE_REFRESH) {
                    currentState = state;
                    onStateChanged(currentState);
                    moveToState(STATE_IDEL);
                    return true;
                }
                return false;
        }
        return false;
    }

    protected abstract boolean isValidOffset(int currentOffset);

    protected abstract int transform(int currentOffset);

    protected abstract int readyRefreshOffset();

    protected abstract void startRefreshing();

    protected abstract void onStateChanged(int state);
}
