package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

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

    private static final int MODE_FOLLOW_CONTENT = 0;
    private static final int MODE_STILL = 1;

    private int mode = 0;
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
        if (isFirstLayout) {
            getContentBehavior().setVisibleHeight(getVisibleHeight());
            getContentBehavior().setHeaderHeight(child.getHeight());
            isFirstLayout = false;
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, child.getHeight(), type == TYPE_TOUCH);
            }
        }
        return start;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
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
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        int height = child.getHeight();
        for (ScrollListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, height + getTopAndBottomOffset(), child.getHeight(), type == TYPE_TOUCH);
        }
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        cancelAnimation();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ContentBehavior;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        int offset = 0;
        if (behavior instanceof ContentBehavior) {
            offset = ((ContentBehavior) behavior).getTopAndBottomOffset() - child.getBottom();
        }
        if (offset != 0) {
            // todo: use TYPE_TOUCH or not
            consumeOffset(parent, child, offset, TYPE_TOUCH);
            return true;
        }
        return false;
    }

    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offset, int type) {
        int current = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, child.getHeight() + current, child.getHeight(), type == TYPE_TOUCH);
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
            l.onScroll(coordinatorLayout, child, child.getHeight() + current, offset, child.getHeight(), type == TYPE_TOUCH);
        }
        return consumed;
    }

    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        return mode == MODE_FOLLOW_CONTENT ? offset : 0;
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
     *
     * @return
     */
    protected boolean isVisible() {
        return getTopAndBottomOffset() >= -invisibleHeight;
    }

    private View findDependencyChild(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ContentBehavior) {
                return v;
            }
        }
        return null;
    }

    private ContentBehavior findDependencyBehavior(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ContentBehavior) {
                return (ContentBehavior) p.getBehavior();
            }
        }
        return null;
    }

    public ContentBehavior getContentBehavior() {
        return findDependencyBehavior(getParent(), getChild());
    }

    public View getContentChild() {
        return findDependencyChild(getParent(), getChild());
    }
}
