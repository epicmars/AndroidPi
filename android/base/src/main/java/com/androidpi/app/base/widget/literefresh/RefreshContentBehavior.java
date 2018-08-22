package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class RefreshContentBehavior<V extends View> extends ContentBehavior<V> implements AnimationOffsetBehavior.ScrollListener,
        Refresher {

    private static final long HOLD_ON_DURATION = 500L;
    private static final long EXIT_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private static final int STATE_IDEL = 0;
    private static final int STATE_START = 1;
    private static final int STATE_READY = 2;
    private static final int STATE_REFRESH = 3;
    private static final int STATE_COMPLETE = 4;

    private DecelerateInterpolator downInterpolator = new DecelerateInterpolator(1.5f);
    private List<OnPullListener> mPullListeners = new ArrayList<>();
    private List<OnRefreshListener> mRefreshListeners = new ArrayList<>();
    private int currentState = STATE_IDEL;

    public RefreshContentBehavior(Context context) {
        this(context, null);
    }

    public RefreshContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addScrollListener(this);
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch) {
        for (OnPullListener l : mPullListeners) {
            l.onStartPulling(max, isTouch);
        }
        moveToState(STATE_START);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch) {
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
        for (OnPullListener l : mPullListeners) {
            l.onPulling(current, delta, max, isTouch);
        }
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
        for (OnPullListener l : mPullListeners) {
            l.onStopPulling(current, max);
        }
        if (current >= readyRefreshOffset()) {
            startRefreshing();
        } else {
            stopScroll((V) child, false);
        }
    }

    private void startRefreshing() {
        if (!mRefreshListeners.isEmpty() && moveToState(STATE_REFRESH)) {
            show();
        } else {
            reset();
        }
    }

    private Runnable offsetCallback;

    private void stopScroll(boolean holdOn) {
        stopScroll(getChild(), holdOn);
    }

    protected void stopScroll(@NonNull V child, boolean holdOn) {
        if (getTopAndBottomOffset() > headerVisibleHeight) {
            if (child.getHandler() == null) return;
            child.getHandler().removeCallbacks(offsetCallback);
            offsetCallback = new Runnable() {
                @Override
                public void run() {
                    reset();
                }
            };
            child.postOnAnimationDelayed(offsetCallback, holdOn ? HOLD_ON_DURATION : 0L);
        }
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        if (mPullListeners != null) {
            mPullListeners.clear();
        }
        if (mRefreshListeners != null) {
            mRefreshListeners.clear();
        }
    }


    @Override
    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        int consumed = offset;
        if (current >= 0 && offset > 0) {
            float y = downInterpolator.getInterpolation(current / (float) parentHeight);
            consumed = (int) ((1f - y) * offset);
        }
        return consumed;
    }

    private int readyRefreshOffset() {
        return headerHeight;
    }

    @Override
    public void refresh() {
        // To avoid unnecessary task enqueueing.
        if (currentState == STATE_REFRESH)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (moveToState(STATE_REFRESH)) {
                    show();
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
        runOnUiThread(new Runnable() {
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

    /**
     * This will reset the header view to it's original position when it's laid out for the first time.
     */
    protected void reset() {
        if (null == getChild()) return;
        float offset = headerVisibleHeight - getChild().getTop();
        if (offset >= 0) return;
        animateOffsetWithDuration(getParent(), getChild(), getTopAndBottomOffset() + (int) offset, EXIT_DURATION);
    }

    /**
     * Make the header view entirely visible.
     */
    protected void show() {
        show(RESET_DURATION);
    }

    protected void show(long animateDuration) {
        if (null == getChild()) return;
        float offset = readyRefreshOffset() - getChild().getTop();
        animateOffsetWithDuration(getParent(), getChild(), getTopAndBottomOffset() + (int) offset, animateDuration);
    }

    public void addOnPullingListener(OnPullListener listener) {
        if (null == listener)
            return;
        mPullListeners.add(listener);
    }

    public void removeOnPullingListener(OnPullListener listener) {
        if (null == listener)
            return;
        mPullListeners.remove(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        if (null == listener)
            return;
        mRefreshListeners.add(listener);
    }

    public void addOnRefreshListeners(Collection<OnRefreshListener> listeners) {
        if (null == listeners || listeners.isEmpty()) return;
        mRefreshListeners.addAll(listeners);
    }

    public void removeRefreshListener(OnRefreshListener listener) {
        if (null == listener)
            return;
        mRefreshListeners.remove(listener);
    }
}
