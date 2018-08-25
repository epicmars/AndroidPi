package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class RefreshStateMachine implements AnimationOffsetBehavior.ScrollListener, Refresher {

    public interface RefreshStateListener {
        void onStateChanged(int state);
    }

    public interface OffsetTransformer {

        boolean isValidOffset(int currentOffset);

        int transform(int currentOffset);

        int readyRefreshOffset();

        boolean hasRefreshListeners();
    }

    static final int STATE_IDLE = 0;
    static final int STATE_START = 1;
    static final int STATE_READY = 2;
    static final int STATE_REFRESH = 3;
    static final int STATE_REFRESH_AND_RESET = 4;
    static final int STATE_COMPLETE = 5;
    static final int STATE_CANCELLED = 6;

    protected int currentState = STATE_IDLE;

    private List<RefreshStateListener> refreshStateListeners;
    private OffsetTransformer transformer;

    public RefreshStateMachine(OffsetTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        if (!isTouch) return;
        moveToState(STATE_START);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (isTouch) {
            if (!transformer.isValidOffset(current))
                return;
            if (transformer.transform(current) >= transformer.readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        if (isTouch) {
            if (!transformer.isValidOffset(current))
                return;
            if (transformer.transform(current) >= transformer.readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        if (!isTouch || !transformer.isValidOffset(current)) {
            return;
        }
        if (transformer.transform(current) < transformer.readyRefreshOffset()) {
            moveToState(STATE_CANCELLED);
            return;
        }
        // If there are refresh listeners and we have reach beyond the trigger point.
        if (!(transformer.hasRefreshListeners() && moveToState(STATE_REFRESH))) {
            // If not refresh, then reset.
            if (!moveToState(STATE_REFRESH_AND_RESET)) {
                moveToState(STATE_CANCELLED);
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
                if (currentState == STATE_COMPLETE || currentState == STATE_CANCELLED || currentState == STATE_REFRESH_AND_RESET) {
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
            case STATE_REFRESH_AND_RESET:
                if (currentState == STATE_REFRESH) {
                    currentState = state;
                    onStateChanged(currentState);
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (currentState == STATE_REFRESH || currentState == STATE_REFRESH_AND_RESET) {
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

    void addRefreshStateListener(RefreshStateListener listener) {
        if (listener == null) return;
        if (refreshStateListeners == null) {
            refreshStateListeners = new ArrayList<>();
        }
        refreshStateListeners.add(listener);
    }

    void onStateChanged(int state) {
        if (refreshStateListeners == null) return;
        for (RefreshStateListener listener : refreshStateListeners) {
            listener.onStateChanged(state);
        }
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
