package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * A behavior for scrollable child of {@link CoordinatorLayout}.
 * <p>
 * View with this behavior must be a direct child of {@link CoordinatorLayout}.
 * <p>
 *
 * The content can cover the header now, there is no need to measure it again.
 *
 * Created by jastrelax on 2017/11/16.
 */

public class RefreshContentBehavior<V extends View> extends ViewOffsetBehavior<V> {

    public RefreshContentBehavior() {
    }

    public RefreshContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof HeaderBehavior
                    || behavior instanceof FooterBehavior;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        int offset = 0;
        if (behavior instanceof HeaderBehavior) {
            offset = dependency.getBottom() - child.getTop();
        } else if (behavior instanceof FooterBehavior) {
            offset = dependency.getTop() - child.getBottom();
        }
        if (offset != 0) {
            setTopAndBottomOffset(getTopAndBottomOffset() + offset);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        // When scrolling up, if content view lay below header,
        // then move the content view until it fully overlay header view.
        if (dy > 0) {
            if (child.getTop() <= 0) return;
            // scroll up
            int offset = MathUtils.clamp(-dy, -child.getTop(), 0);
            if (offset == 0)
                return;
            setTopAndBottomOffset(getTopAndBottomOffset() + offset);
            consumed[1] = -offset;
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // When scrolling down, if there is unconsumed pixels and content view overlap header view.
        // Make use of these pixels to move content view to make header fully visible.
        if (dyUnconsumed < 0) {
            HeaderBehavior behavior = findDependencyHeaderBehavior(coordinatorLayout, child);
            if (behavior == null || child.getTop() >= behavior.visibleHeight) return;
            int offset = MathUtils.clamp(-dyUnconsumed, 0, (int) behavior.visibleHeight - child.getTop());
            if (offset == 0)
                return;
            setTopAndBottomOffset(getTopAndBottomOffset() + offset);
        }
    }

    private HeaderBehavior findDependencyHeaderBehavior(CoordinatorLayout parent, View child) {
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof HeaderBehavior) {
                return (HeaderBehavior) p.getBehavior();
            }
        }
        return null;
    }
}
