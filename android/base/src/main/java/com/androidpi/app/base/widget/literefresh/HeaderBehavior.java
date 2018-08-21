package com.androidpi.app.base.widget.literefresh;

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
 * <p>
 * With some configuration, the view with which this behavior associated can move following
 * the nested scrolling content or fix in it's original position.
 * <p>
 * The content view can only nested with the visible part of header view.
 * <p>
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private static final long EXIT_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private int childHeight;
    private boolean isFirstLayout = true;

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context) {
        super(context);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        childHeight = child.getHeight();
        // Compute fixed offset.
        final float fixedOffset = -childHeight + visibleHeight;
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
        // The action is scroll along vertical axes.
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (!started) return false;

        // If content view cover the header, do not start the nested scroll.
        ContentBehavior contentBehavior = getContentBehavior(coordinatorLayout);
        if (contentBehavior != null && contentBehavior.getMode() == ContentBehavior.MODE_OVERLAP) {
            started = contentBehavior.getTopAndBottomOffset() >= visibleHeight;
            if (!started) return false;
        }

        cancelAnimation();
        boolean isTouch = (type == TYPE_TOUCH);
        for (ScrollListener l : mListeners) {
            l.onStartScroll(coordinatorLayout, child, (int) maxOffset, isTouch);
        }
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int bottom = child.getBottom();
        // Height of child may have changed.
        float maxOffset = Math.max(this.maxOffset, child.getHeight());
        // Scrolling may triggered by a fling, we only care about human touch.
        ContentBehavior contentBehavior = getContentBehavior(coordinatorLayout);
        if (contentBehavior == null) return;
        float offset = 0;
        if (contentBehavior.getMode() == ContentBehavior.MODE_OVERLAP) {
            // The header is fixed now.
            // Only when content lay below header will work here.
            if (type == TYPE_TOUCH && isHiddenPartVisible()) {
                // If visible, when scrolling up it will consume the scroll range until it's invisible.
                if (dy > 0) {
                    // scroll up
                    offset = MathUtils.clamp(-dy, -bottom + visibleHeight, 0);
                }
            }
        } else {
            if (dy > 0) {
                // scroll up
                // The header can be entirely hidden.
                if (bottom > 0) {
                    offset = MathUtils.clamp(-dy, -bottom, 0);
                }
            }
        }

        if (type == TYPE_TOUCH && isVisible()) {
            // If entirely visible, when scrolling down it will consume the scroll range until it reach maximum offset
            if (dy < 0) {
                // scroll down
                offset = MathUtils.clamp(-dy, 0, maxOffset - bottom);
            }
        }

        consumeOffset(coordinatorLayout, child, (int) offset, type);
        consumed[1] = -(int) offset;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        ContentBehavior contentBehavior = getContentBehavior(coordinatorLayout);
        if (contentBehavior == null) return;
        if (contentBehavior.getMode() == ContentBehavior.MODE_FOLLOW) {
            // If mode is follow and is scrolling up.
            if (dyUnconsumed > 0) {
                // The scrolling up range not consumed by the scrolling view is consumed by the header.
                // Until it's entirely invisible.
                if (child.getBottom() > 0) {
                    int offset = MathUtils.clamp(-dyUnconsumed, -child.getBottom(), 0);
                    consumeOffset(coordinatorLayout, child, offset, type);
                }
            } else if (dyUnconsumed < 0) {
                // If scrolling down
                if (child.getBottom() < visibleHeight) {
                    int offset = MathUtils.clamp(-dyUnconsumed, 0, (int)(visibleHeight - child.getBottom()));
                    consumeOffset(coordinatorLayout, child, offset, type);
                }
            }
        } else {
            // When the scrolling content lay below header.
            if (contentBehavior.getTopAndBottomOffset() < visibleHeight)
                return;
            if (type == TYPE_TOUCH) {
                // If header is invisible when scrolling down, we expect it to be visible.
                // So the range not consumed by the scrolling view is consumed by the header.
                if (!isHiddenPartVisible()) {
                    if (dyUnconsumed < 0) {
                        int offset = MathUtils.clamp(-dyUnconsumed, 0, (int)(maxOffset - child.getBottom()));
                        consumeOffset(coordinatorLayout, child, offset, type);
                    }
                }
            }
        }
    }

    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offset, int type) {
        int current = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, child.getHeight() + current, (int) maxOffset, type == TYPE_TOUCH);
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
        // If header should be hidden entirely, consume the fling.
        // Otherwise, do nothing.
        if (isHiddenPartVisible() && visibleHeight == 0) {
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
            boolean isTouch = (type == TYPE_TOUCH);
            for (ScrollListener l : mListeners) {
                l.onStopScroll(coordinatorLayout, child, height + getTopAndBottomOffset(), (int) maxOffset, isTouch);
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
        if (null == getChild()) return;
        float offset = -getChild().getTop();
        animateOffsetWithDuration(getParent(), getChild(), getTopAndBottomOffset() + (int) offset, animateDuration);
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        cancelAnimation();
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

    private ContentBehavior getContentBehavior(CoordinatorLayout parent) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            if (behavior instanceof ContentBehavior) {
                ContentBehavior contentBehavior = (ContentBehavior) behavior;
                return contentBehavior;
            }
        }
        return null;
    }

    /**
     * Tell if the hidden part of header view is visible.
     * If invisible height is zero, it can be considered invisible.
     *
     * @return true if hidden part of header view is visible,
     * otherwise return false.
     */
    protected boolean isHiddenPartVisible() {
        return getTopAndBottomOffset() > -invisibleHeight;
    }

    /**
     * Tell if the visible part of header view is entirely visible.
     * @return
     */
    protected boolean isVisible() {
        return getTopAndBottomOffset() >= -invisibleHeight;
    }

}
