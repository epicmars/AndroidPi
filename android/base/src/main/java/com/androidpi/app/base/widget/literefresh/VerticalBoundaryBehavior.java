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
 * Super class of header and footer behavior.
 * <br>
 * The header and footer behaviors are almost the same, the main difference is
 * they have different coordinate system when we trace the bottom and top position
 * of the view to which they attached.
 * view.
 * <br>
 * We have traced the bottom and top offset of the view using the view's default coordinate
 * system, the original point is the left top position of the parent, and when we move to right
 * or bottom from the original point we get positive range, otherwise it's negative.
 *
 * <br>
 * The coordinate system of the bottom of the view which header behavior is attached
 * would be a matrix transformation below:
 *
 * <pre>
 *      |1 0 height||x|   |         x|
 *      |0 1 height||y| = |y + height|
 *      |0 0      1||1|   |         1|
 * </pre>
 *
 * then the coordinate system of the top of footer behavior view
 * would be a transformation below:
 *
 * <pre>
 *      |1   0              0||x|   |                x|
 *      |0   -1  parentHeight||y| = |-y + parentHeight|
 *      |0   0              1||1|   |                1|
 * </pre>
 *
 * Created by jastrelax on 2018/8/23.
 */
public abstract class VerticalBoundaryBehavior<V extends View> extends AnimationOffsetBehavior<V> {


    public VerticalBoundaryBehavior() {
    }

    public VerticalBoundaryBehavior(Context context) {
        super(context);
    }

    public VerticalBoundaryBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        int height = child.getHeight();
        int parentHeight = coordinatorLayout.getHeight();
        for (ScrollListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, transformOffsetCoordinate(getTopAndBottomOffset(), height, parentHeight), height, type == TYPE_TOUCH);
        }
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
            ContentBehavior contentBehavior = (ContentBehavior) behavior;
            offset = computeOffsetOnDependentViewChanged(parent, child, dependency, contentBehavior);
        }
        if (offset != 0) {
            // todo: use TYPE_TOUCH or not
            consumeOffsetOnDependentViewChanged(parent, child, offset, TYPE_TOUCH);
            return true;
        }
        return false;
    }

    private int consumeOffsetOnDependentViewChanged(CoordinatorLayout coordinatorLayout, View child, int offset, int type) {
        int current = getTopAndBottomOffset();
        int parentHeight = coordinatorLayout.getHeight();
        int height = child.getHeight();
        // Before child consume the offset.
        for (ScrollListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, transformOffsetCoordinate(current, height, parentHeight), height, type == TYPE_TOUCH);
        }
        int consumed = consumeOffsetOnDependentViewChanged(current, parentHeight, offset);
        current += consumed;
        setTopAndBottomOffset(current);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's onNestedScroll().
        // The header view itself can make some transformation by setTranslationY() that may keep it's drawing rectangle.
        // Such as when scroll down, use setTranslationY() with negative value.
        // In this case CoordinatorLayout will not call onDependentViewChanged().
        // So We need to call onDependentViewChanged() manually.
        // coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, transformOffsetCoordinate(current, height, parentHeight), offset, height, type == TYPE_TOUCH);
        }
        return consumed;
    }

    protected abstract int computeOffsetOnDependentViewChanged(CoordinatorLayout parent, V child, View dependency, ContentBehavior contentBehavior);

    protected abstract int consumeOffsetOnDependentViewChanged(int current, int parentHeight, int offset);

    protected abstract int transformOffsetCoordinate(int current, int height, int parentHeight);

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

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        cancelAnimation();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }
}
