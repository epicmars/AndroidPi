package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class RefreshStateMachine implements AnimationOffsetBehavior.ScrollingListener, Refresher {

    public interface RefreshStateHandler {

        /**
         * Offset that can makes an indicator's invisible part visible.
         * @param currentOffset
         * @return
         */
        boolean isValidOffset(int currentOffset);

        /**
         * Transform content's offset coordinate to indicator's coordinate system.
         * @param currentOffset current vertical offset of content view.
         * @return transformed offset in indicator's coordinate system.
         */
        int transform(int currentOffset);

        /**
         * A positive offset range in indicator's coordinate system, if content's vertical
         * offset has reach this point then it can trigger a refresh state.
         */
        int readyRefreshOffset();

        /**
         * If a controller has any refresh state listeners.
         * @return
         */
        boolean hasRefreshListeners();

        /**
         * A callback method when refresh state has changed.
         * @param state
         * @param throwable
         */
        void onStateChanged(int state, Throwable throwable);
    }

    static final int STATE_IDLE = 0;
    static final int STATE_START = 1;
    static final int STATE_READY = 2;
    static final int STATE_CANCELLED = 3;
    static final int STATE_CANCELLED_RESET = 4;
    static final int STATE_REFRESH = 5;
    static final int STATE_REFRESH_RESET = 6;
    static final int STATE_COMPLETE = 7;
    static final int STATE_IDLE_RESET = 8;

    protected int currentState = STATE_IDLE;

    private RefreshStateHandler stateHandler;

    public RefreshStateMachine(RefreshStateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        Timber.d("onStartScroll: isTouch %b", isTouch);
        // If current state is ready, when touch event is MotionEvent.ACTION_UP, may trigger a fling that start another scroll immediately.
        if (!isTouch && currentState == STATE_READY)
            return;
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
        if (!stateHandler.isValidOffset(current)) {
//            Timber.d("not valid: %d", current);
            return;
        }
        // todo: what if transformed offset is already larger than or equal to ready offset.
        if (stateHandler.transform(current) >= stateHandler.readyRefreshOffset()) {
            if (!moveToState(STATE_READY)) {
                moveToState(STATE_IDLE_RESET);
            }
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

    protected boolean moveToState(int state, Throwable throwable) {
        switch (state) {
            case STATE_IDLE:
                if (currentState == STATE_COMPLETE
                        || currentState == STATE_CANCELLED
                        || currentState == STATE_CANCELLED_RESET) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    return true;
                }
                return false;
            case STATE_START:
                if (currentState == STATE_IDLE_RESET || currentState == STATE_IDLE || currentState == STATE_READY) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    return true;
                }
                return false;
            case STATE_READY:
                if (currentState == STATE_START) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    return true;
                }
                return false;
            case STATE_CANCELLED:
                if (currentState == STATE_START || currentState == STATE_READY) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    moveToState(STATE_IDLE);
                    return true;
                }
                return false;
            case STATE_CANCELLED_RESET:
                if (currentState == STATE_START || currentState == STATE_READY || currentState == STATE_IDLE_RESET) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    moveToState(STATE_IDLE);
                    return true;
                }
                return false;
            case STATE_REFRESH:
                if (currentState == STATE_READY || currentState == STATE_IDLE) {
                    currentState = STATE_REFRESH;
                    onStateChanged(currentState, throwable);
                    return true;
                }
                return false;
            case STATE_REFRESH_RESET:
                if (currentState == STATE_REFRESH || currentState == STATE_REFRESH_RESET) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (currentState == STATE_REFRESH || currentState == STATE_REFRESH_RESET) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    moveToState(STATE_IDLE);
                    return true;
                }
                return false;
            case STATE_IDLE_RESET:
                if (currentState == STATE_IDLE) {
                    currentState = state;
                    onStateChanged(currentState, throwable);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * Try to move to another state.
     *
     * @param state
     * @return
     */
    protected boolean moveToState(int state) {
        return moveToState(state, null);
    }

    void onStateChanged(int state, Throwable throwable) {
        if (stateHandler == null) return;
        stateHandler.onStateChanged(state, throwable);
    }

    boolean isRefreshing() {
        return currentState == STATE_REFRESH || currentState == STATE_REFRESH_RESET;
    }

    @Override
    public void refresh() {
        moveToState(STATE_REFRESH);
    }

    @Override
    public void refreshComplete() {
        refreshCompleted(null);
    }

    @Override
    public void refreshError(Throwable throwable) {
        refreshCompleted(throwable);
    }

    private void refreshCompleted(Throwable throwable) {
        moveToState(STATE_COMPLETE, throwable);
    }

}
