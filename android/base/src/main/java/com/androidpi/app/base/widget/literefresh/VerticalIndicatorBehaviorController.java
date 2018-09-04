package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/27.
 */
public abstract class VerticalIndicatorBehaviorController<B extends VerticalIndicatorBehavior> extends BehaviorController<B> {

    /**
     * Follow content view.
     */
    public static final int MODE_FOLLOW = 0;
    /**
     * Still, does not follow content view.
     */
    public static final int MODE_STILL = 1;

    /**
     * Follow when scroll down.
     */
    public static final int MODE_FOLLOW_DOWN = 2;

    /**
     * Follow when scroll up.
     */
    public static final int MODE_FOLLOW_UP = 3;

    protected int mode = MODE_FOLLOW;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public VerticalIndicatorBehaviorController(B behavior) {
        super(behavior);
    }

    public abstract int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent, View child, View dependency, VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior);

    public abstract float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int currentOffset, int offsetDelta);

    public abstract int transformOffsetCoordinate(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior, int currentOffset);

    /**
     * Tell if the hidden part of the view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * in that case it's considered to be invisible.
     *
     * @return true if hidden part of view is visible,
     * otherwise return false.
     */
    public abstract boolean isHiddenPartVisible(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior);
}
