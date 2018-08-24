package com.androidpi.app.base.widget.literefresh;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class ContentBehaviorController extends BehaviorController<ContentBehavior> {

    private static final long HOLD_ON_DURATION = 500L;
    private static final long EXIT_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private static final int STATE_IDEL = 0;
    private static final int STATE_START = 1;
    private static final int STATE_READY = 2;
    private static final int STATE_REFRESH = 3;
    private static final int STATE_COMPLETE = 4;

    private int currentState = STATE_IDEL;

    public ContentBehaviorController(ContentBehavior behavior) {
        super(behavior);
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        super.onStartScroll(coordinatorLayout, child, max, isTouch);
        moveToState(STATE_START);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        super.onPreScroll(coordinatorLayout, child, current, max, isTouch);
        if (isTouch) {
            if (current >= readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch) {
        super.onScroll(coordinatorLayout, child, current, delta, max, isTouch);
        if (isTouch) {
            if (current >= readyRefreshOffset()) {
                moveToState(STATE_READY);
            } else {
                moveToState(STATE_START);
            }
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
        super.onStopScroll(coordinatorLayout, child, current, max, isTouch);
        if (current >= readyRefreshOffset()) {
            startRefreshing();
        } else {
            stopScroll(child, false);
        }
    }

    private void startRefreshing() {
        if (!mRefreshListeners.isEmpty() && moveToState(STATE_REFRESH)) {
            show(readyRefreshOffset(), EXIT_DURATION);
        } else {
            reset(RESET_DURATION);
        }
    }

    private Runnable offsetCallback;

    private void stopScroll(boolean holdOn) {
        stopScroll(behavior.getChild(), holdOn);
    }

    protected void stopScroll(@NonNull View child, boolean holdOn) {
        int currentOffset = behavior.getTopAndBottomOffset();
        // If content offset is large header's visible height or smaller than zero,
        // which means content has scrolled to a insignificant or invalid position.
        if (currentOffset > behavior.headerVisibleHeight || currentOffset < 0) {
            if (child.getHandler() == null) return;
            // Remove previous pending callback.
            child.getHandler().removeCallbacks(offsetCallback);
            offsetCallback = new Runnable() {
                @Override
                public void run() {
                    reset(RESET_DURATION);
                }
            };
            child.postOnAnimationDelayed(offsetCallback, holdOn ? HOLD_ON_DURATION : 0L);
        }
    }


    @Override
    public void refresh() {
        // To avoid unnecessary task enqueueing.
        if (currentState == STATE_REFRESH)
            return;
        behavior.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (moveToState(STATE_REFRESH)) {
                    show(readyRefreshOffset(), EXIT_DURATION);
                }
            }
        });
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
        behavior.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moveToState(STATE_COMPLETE);
            }
        });
    }

    /**
     * Try to move to another state.
     *
     * @param state
     * @return
     */
    private boolean moveToState(int state) {
        switch (state) {
            case STATE_IDEL:
                if (currentState == STATE_COMPLETE) {
                    currentState = STATE_IDEL;
                    return true;
                }
                return false;
            case STATE_START:
                if (currentState == STATE_IDEL || currentState == STATE_READY) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefreshStart();
                    }
                    return true;
                }
                return false;
            case STATE_READY:
                if (currentState == STATE_START) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onReleaseToRefresh();
                    }
                    return true;
                }
                return false;
            case STATE_REFRESH:
                if (currentState != STATE_REFRESH) {
                    currentState = STATE_REFRESH;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefresh();
                    }
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (currentState == STATE_REFRESH) {
                    currentState = state;
                    for (OnRefreshListener l : mRefreshListeners) {
                        l.onRefreshEnd();
                    }
                    stopScroll(true);
                    moveToState(STATE_IDEL);
                    return true;
                }
                return false;
        }
        return false;
    }

    private int readyRefreshOffset() {
        return behavior.headerHeight;
    }

    /**
     * This will reset the header or footer view to it's original position when it's laid out for the first time.
     */
    protected void reset(long animateDuration) {
        if (null == behavior.getChild() || behavior.getParent() == null) return;
        // Reset footer first, then consider header.
        // Based on a strong contract that headerVisibleHeight is a distance from parent top.
        int offset;
        if (- behavior.getChild().getBottom() + behavior.getParent().getHeight() > 0) {
            offset = behavior.getParent().getHeight() - behavior.getChild().getBottom();
        } else {
            offset = behavior.headerVisibleHeight - behavior.getTopAndBottomOffset();
        }
        behavior.animateOffsetWithDuration(behavior.getParent(), behavior.getChild(), behavior.getTopAndBottomOffset() + offset, animateDuration);
    }

    /**
     * Make the header view entirely visible.
     */
    protected void show(int readyRefreshOffset, long animateDuration) {
        if (null == behavior.getChild()) return;
        float offset = readyRefreshOffset - behavior.getChild().getTop();
        behavior.animateOffsetWithDuration(behavior.getParent(), behavior.getChild(), behavior.getTopAndBottomOffset() + (int) offset, animateDuration);
    }
}
