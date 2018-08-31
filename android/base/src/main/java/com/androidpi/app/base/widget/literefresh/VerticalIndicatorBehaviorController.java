package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/27.
 */
public abstract class VerticalIndicatorBehaviorController<B extends VerticalIndicatorBehavior> extends BehaviorController<B> {

    protected int mode = -1;
    public void setMode(int mode) {
        this.mode = mode;
    }

    public VerticalIndicatorBehaviorController(B behavior) {
        super(behavior);
    }

    public abstract int computeOffsetDeltaOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, CoordinatorLayout parent, View child, View dependency);

    public abstract float consumeOffsetOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int parentHeight, int height, int currentOffset, int offsetDelta);

    public abstract int transformOffsetCoordinate(VerticalIndicatorBehavior behavior, int currentOffset, int height, int parentHeight);

    /**
     * Tell if the hidden part of the view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * in that case it's considered to be invisible.
     *
     * @return true if hidden part of view is visible,
     * otherwise return false.
     */
    public abstract boolean isHiddenPartVisible(VerticalIndicatorBehavior behavior);
}
