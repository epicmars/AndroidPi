package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

import java.util.List;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Super class of header and footer behavior.
 * <p>
 * The header and footer behaviors are almost the same, the main difference is that
 * they have different coordinate system when we trace the bottom and top position
 * of the view to which they attached respectively.
 * view.
 * <p>
 * As we have record the bottom and top offset of the view using the view's default coordinate
 * system, whose original point is the left top point of the parent view. Relative to that original
 * point the right and bottom position is positive.
 * <p>
 * Now we need to trace how much the header has scroll from the top of the parent view.
 * We need to transform the bottom position of the view to which the header behavior is attached
 * in the coordinate system of the parent view to another one. This coordinate system would be a
 * affine matrix transformation below:
 * <p>
 * <pre>
 *      |1 0 height||x|   |         x|
 *      |0 1 height||y| = |y + height|
 *      |0 0      1||1|   |         1|
 * </pre>
 * <p>
 * And we also need to trace how much the footer view has scrolled from the bottom of the parent
 * view, We use top position of the view as the traced point, the coordinate system would be a
 * affine transformation below:
 * <p>
 * <pre>
 *      |1   0              0||x|   |                x|
 *      |0   -1  parentHeight||y| = |-y + parentHeight|
 *      |0   0              1||1|   |                1|
 * </pre>
 * <p>
 * Created by jastrelax on 2018/8/23.
 */
public abstract class VerticalIndicatorBehavior<V extends View, CTR extends VerticalIndicatorBehaviorController> extends AnimationOffsetBehavior<V, CTR> {

    private final int defaultMinTriggerRange;

    public VerticalIndicatorBehavior(Context context) {
        this(context, null);
    }

    public VerticalIndicatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OffsetBehavior, 0, 0);
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeight)) {
            configuration.setVisibleHeight(Math.round(a.getDimension(R.styleable.OffsetBehavior_lr_visibleHeight, 0)));
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeightRatio)) {
            configuration.setVisibleHeightRatio(a.getFraction(R.styleable.OffsetBehavior_lr_visibleHeightRatio, 1, 1, 0f));
            configuration.setVisibleHeightParentRatio(a.getFraction(R.styleable.OffsetBehavior_lr_visibleHeightRatio, 1, 2, 0f));
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_showUpWhenRefresh)) {
            configuration.setShowUpWhenRefresh(a.getBoolean(R.styleable.OffsetBehavior_lr_showUpWhenRefresh, false));
        }

        configuration.setUseDefinedRefreshTriggerRange(a.hasValue(R.styleable.OffsetBehavior_lr_triggerRange));
        configuration.setRefreshTriggerRange(a.getDimensionPixelOffset(R.styleable.OffsetBehavior_lr_triggerRange, 0));
        a.recycle();
        defaultMinTriggerRange = context.getResources().getDimensionPixelOffset(R.dimen.defaultMinTriggerRange);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (!configuration.isSettled()) {
            cancelAnimation();
            // Compute visible height of child.
            int visibleHeight = (int) Math.max((float) configuration.getVisibleHeight(), configuration.getVisibleHeightParentRatio() > configuration.getVisibleHeightRatio() ? configuration.getVisibleHeightRatio() * parent.getHeight() : configuration.getVisibleHeightRatio() * child.getHeight());
            int invisibleHeight = child.getHeight() - visibleHeight;
            // Compute refresh trigger range.
            if (configuration.getRefreshTriggerRange() < 0) {
                // User define a invalid trigger range, use the default.
                configuration.setRefreshTriggerRange(configuration.getDefaultRefreshTriggerRange());
            } else if (!configuration.isUseDefinedRefreshTriggerRange()){
                // User doesn't predefined one, we need to ensure the refreshing is triggered when indicator is
                // totally visible, no matter whether child height is zero or not. If child is already visible,
                // the invisible height will be non-positive, in this case we use the default .
                if (defaultMinTriggerRange > 0 && invisibleHeight >= defaultMinTriggerRange && invisibleHeight <= configuration.getDefaultRefreshTriggerRange()) {
                    configuration.setRefreshTriggerRange(invisibleHeight);
                } else {
                    configuration.setRefreshTriggerRange(Math.max(invisibleHeight, configuration.getDefaultRefreshTriggerRange()));
                }
            } // Otherwise we use predefined trigger range.
            configuration.setVisibleHeight(visibleHeight);
            configuration.setInvisibleHeight(invisibleHeight);
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollingListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, child.getHeight(), type == TYPE_TOUCH);
            }
        }
        return start;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        // If indicator should be hidden entirely, and hidden part is visible now consume the fling.
        // Otherwise, do nothing.
        if (controller.isHiddenPartVisible(coordinatorLayout, child,this)) {
            return true;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        int height = child.getHeight();
        for (ScrollingListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, controller.transformOffsetCoordinate(coordinatorLayout, child, this, getTopAndBottomOffset()), height, type == TYPE_TOUCH);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ScrollingContentBehavior;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        int offsetDelta = 0;
        if (behavior instanceof ScrollingContentBehavior) {
            ScrollingContentBehavior scrollingContentBehavior = (ScrollingContentBehavior) behavior;
            offsetDelta = controller.computeOffsetDeltaOnDependentViewChanged(parent, child, dependency, this, scrollingContentBehavior);
        }
        if (offsetDelta != 0) {
            // todo: use TYPE_TOUCH or not, may
            consumeOffsetOnDependentViewChanged(parent, child, offsetDelta, TYPE_TOUCH);
            return true;
        }
        return false;
    }

    private void consumeOffsetOnDependentViewChanged(CoordinatorLayout coordinatorLayout, View child, int offsetDelta, int type) {
        int currentOffset = getTopAndBottomOffset();
        int height = child.getHeight();
        // Before child consume the offset.
        for (ScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, controller.transformOffsetCoordinate(coordinatorLayout, child, this, currentOffset), height, type == TYPE_TOUCH);
        }
        float consumed = controller.consumeOffsetOnDependentViewChanged(coordinatorLayout, child, this, getContentBehavior(), currentOffset, offsetDelta);
        currentOffset = Math.round(currentOffset + consumed);
        // If the offset is already at the top don't reset it again.
        setTopAndBottomOffset(currentOffset);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, controller.transformOffsetCoordinate(coordinatorLayout, child, this, currentOffset), offsetDelta, height, type == TYPE_TOUCH);
        }
    }

    private View findDependencyChild(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ScrollingContentBehavior) {
                return v;
            }
        }
        return null;
    }

    private ScrollingContentBehavior findDependencyBehavior(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ScrollingContentBehavior) {
                return (ScrollingContentBehavior) p.getBehavior();
            }
        }
        return null;
    }

    public ScrollingContentBehavior getContentBehavior() {
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
