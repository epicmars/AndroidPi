package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * The header behavior consume some of the scrolled distance dispatch by the nested scrolling
 * content base on several rules.It will not consume the distance made by fling.
 *
 * With some configuration, the view with which this behavior associated can move following
 * the nested scrolling content or fix in it's original position.
 *
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private static final long EXIT_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private int childHeight;
    private int parentHeight;
    private boolean isFirstLayout = true;
    private float fixedOffset;

    public HeaderBehavior(Context context) {
        this(context, null);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        childHeight = child.getHeight();
        parentHeight = parent.getHeight();
        // Compute fixed offset.
        fixedOffset = -childHeight + visibleHeight;
        // Relayout should not change current offset.
        if (isFirstLayout) {
            cancelAnimation();
            setTopAndBottomOffset((int) fixedOffset);
            isFirstLayout = false;
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // The action is pull along vertical axes.
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (started) {
            cancelAnimation();
            for (ScrollListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, (int) maxOffset);
            }
        }
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int bottom = child.getBottom();
        // Height of child may have changed.
        float maxOffset = Math.max(this.maxOffset, child.getHeight());
        // Scrolling may triggered by a fling, we only care about human touch.
        if (type == TYPE_TOUCH) {
            // If header is visible, it will consume the scroll range until it's invisible or reach maximum offset.
            if (isVisible()) {
                float offset = 0;
                if (dy > 0) {
                    // Pulling up.
                    offset = MathUtils.clamp(-dy, -bottom + visibleHeight, 0);
                } else if (dy < 0) {
                    // Pulling down.
                    offset = MathUtils.clamp(-dy, 0, maxOffset - bottom);
                }
                consumeOffset(coordinatorLayout, child, (int) offset);
                consumed[1] = -(int) offset;
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == TYPE_TOUCH) {
            // If header is invisible and the scrolling content has reached it's top,
            // The pulling down range not consumed by the scrolling view is consumed by the header.
            if (isInvisible()) {
                if (dyUnconsumed < 0) {
                    int offset = MathUtils.clamp(-dyUnconsumed, 0, (int)(maxOffset - child.getBottom()));
                    consumeOffset(coordinatorLayout, child, offset);
                }
            }
        }
    }

    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offset) {
        int current = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, child.getHeight() + current, (int) maxOffset);
        }
        int consumed = onConsumeOffset(current, coordinatorLayout.getHeight(), offset);
        current += consumed;
        setTopAndBottomOffset(current);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's onNestedScroll().
        // The header view itself can make some transformation by setTranslationY() that may keep it's drawing rectangle.
        // In this case CoordinatorLayout will not call onDependentViewChanged().
        // So We need to call onDependentViewChanged() manually.
        coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, child.getHeight() + current, offset, (int) maxOffset, true);
        }
        return consumed;
    }

    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        return offset;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        // If header should be hidden, consume the fling.
        // Otherwise, do nothing.
        if (isVisible() && visibleHeight == 0) {
            return true;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (type == TYPE_TOUCH) {
            int height = child.getHeight();
            for (ScrollListener l : mListeners) {
                l.onStopScroll(coordinatorLayout, child, height + getTopAndBottomOffset(), (int) maxOffset);
            }
        }
    }

    /**
     * This will reset the header view to it's original position when it's laid out for the first time.
     */
    protected void hide() {
        float offset = -getChild().getBottom() + visibleHeight;
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
        float offset = -getChild().getTop();
        animateOffsetWithDuration(getParent(), getChild(), getTopAndBottomOffset() + (int) offset, animateDuration);
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }

    private boolean isCompleteVisible() {
        return getTopAndBottomOffset() >= 0;
    }

    private boolean isPartialVisible() {
        int offset = getTopAndBottomOffset();
        return offset > -childHeight && offset < 0;
    }

    /**
     * Tell if the invisible part of header view is visible.
     * @return
     */
    protected boolean isVisible() {
        return getTopAndBottomOffset() > -childHeight + visibleHeight;
    }

    protected boolean isInvisible() {
        return !isVisible();
    }
}
