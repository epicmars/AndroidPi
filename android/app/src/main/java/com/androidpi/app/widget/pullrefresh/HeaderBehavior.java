package com.androidpi.app.widget.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private static final long EXIT_DURATION = 300L;
    private static final long HOLD_ON_DURATION = 1000L;
    private DecelerateInterpolator inInterpolator = new DecelerateInterpolator(2f);

    public interface HeaderListener {

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max);
    }

    private List<HeaderListener> mListeners = new ArrayList<>();
    private CoordinatorLayout mParent;
    private V mChild;

    private int DEFAULT_HEIGHT;
    private boolean isFirstLayout = true;

    public HeaderBehavior() {
        this(null, null);
    }

    public HeaderBehavior(Context context) {
        this(context, null);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void addHeaderListener(HeaderListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    protected void removeHeaderListener(HeaderListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (mParent == null) {
            mParent = parent;
        }
        if (mChild == null) {
            mChild = child;
        }
        if (DEFAULT_HEIGHT == 0) {
            DEFAULT_HEIGHT = child.getHeight();
        }
        // Relayout should not change current offset.
        if (isFirstLayout) {
            cancelAnimation();
            setTopAndBottomOffset(-DEFAULT_HEIGHT);
            isFirstLayout = false;
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // The action is pull along vertical axes.
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        //
        if (started) {
            cancelAnimation();
        }
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int top = child.getTop();
        int bottom = child.getBottom();
        int height = child.getHeight();
        int parentHeight = coordinatorLayout.getHeight();
        // Scrolling may triggered by a fling, we only care about human touch.
        if (type == TYPE_TOUCH) {
            // If header is visible, it will consume the scroll range until it's invisible.
            if (isVisible()) {
                int offset = 0;
                if (dy > 0) {
                    // Pulling up.
                    offset = MathUtils.clamp(-dy, -bottom, 0);
                } else if (dy < 0) {
                    // Pulling down.
                    offset = MathUtils.clamp(-dy, 0, parentHeight - (top + height));
                }
                if (offset != 0) {
                    for (HeaderListener l : mListeners) {
                        l.onPreScroll(coordinatorLayout, child, parentHeight);
                    }
                    int consumedOffset = offsetTopAndBottom(coordinatorLayout, child, offset);
                    consumed[1] = -offset;
                }
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        int top = child.getTop();
        if (type == TYPE_TOUCH) {
            // If header is invisible and the scrolling content has reached it's top,
            // The pulling down range not consumed by the scrolling view is consumed by the header.
            if (isInvisible()) {
                if (dyUnconsumed < 0) {
                    int offset = MathUtils.clamp(-dyUnconsumed, 0, -top);
                    offsetTopAndBottom(coordinatorLayout, child, offset);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (type == TYPE_TOUCH) {
            int height = child.getHeight();
            for (HeaderListener l : mListeners) {
                l.onStopScroll(coordinatorLayout, child, height + getTopAndBottomOffset(), height);
            }
        }
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }

    public CoordinatorLayout getParent() {
        return mParent;
    }

    public V getChild() {
        return mChild;
    }

    protected void resetScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child) {
        if (isVisible()) {
            int offset = - child.getTop();
            animateOffsetDeltaTopAndBottom(coordinatorLayout, child, offset, EXIT_DURATION);
        }
    }

    private Runnable offsetCallback;

    protected void stopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, boolean holdOn) {
        if (isVisible()) {
            int height = child.getHeight();
            int top = child.getTop();
            int offset = - (height + top);
            if (holdOn) {
                child.getHandler().removeCallbacks(offsetCallback);
                offsetCallback = new Runnable() {
                    @Override
                    public void run() {
                        animateOffsetDeltaTopAndBottom(coordinatorLayout, child, offset, EXIT_DURATION);
                    }
                };
                child.postDelayed(offsetCallback, HOLD_ON_DURATION);
            } else {
                animateOffsetDeltaTopAndBottom(coordinatorLayout, child, offset, EXIT_DURATION);
            }
        }
    }

    private void animateOffsetDeltaTopAndBottom(CoordinatorLayout coordinatorLayout, final V child, int offset, long duration) {
        animateOffsetWithDuration(coordinatorLayout, child, getTopAndBottomOffset() + offset, duration);
    }

    private int offsetTopAndBottom(CoordinatorLayout coordinatorLayout, View child, int offset) {
        int current = getTopAndBottomOffset() + offset;
        int height = child.getHeight();
        int consumed = offset;
        if (current > 0 && offset > 0) {
            int parentHeight = coordinatorLayout.getHeight();
            double x = current / (double) parentHeight;
            consumed = (int) (1f - inInterpolator.getInterpolation((float) x) * offset);
        }
        setTopAndBottomOffset(current + consumed);
        for (HeaderListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, height + current, offset, height);
        }
        return consumed;
    }

    private boolean isCompleteVisible() {
        return getTopAndBottomOffset() >= 0;
    }

    private boolean isPartialVisible() {
        int offset = getTopAndBottomOffset();
        return offset > -DEFAULT_HEIGHT && offset < 0;
    }

    private boolean isVisible() {
        return !isInvisible();
    }

    private boolean isInvisible() {
        return getTopAndBottomOffset() <= -DEFAULT_HEIGHT;
    }
}
