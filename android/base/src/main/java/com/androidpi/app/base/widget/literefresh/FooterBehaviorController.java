package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/27.
 */
public class FooterBehaviorController extends VerticalIndicatorBehaviorController {

    public FooterBehaviorController(VerticalIndicatorBehavior behavior) {
        super(behavior);
    }

    @Override
    public int computeOffsetDeltaOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, CoordinatorLayout parent, View child, View dependency) {
        return scrollingContentBehavior.getTopAndBottomOffset() + dependency.getHeight() - child.getTop();
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int parentHeight, int height, int currentOffset, int offsetDelta) {
        return offsetDelta;
    }

    @Override
    public int transformOffsetCoordinate(VerticalIndicatorBehavior behavior, int currentOffset, int height, int parentHeight) {
        // The current offset is the footer's top and bottom offset.
        return -currentOffset + parentHeight;
    }

    @Override
    public boolean isHiddenPartVisible(VerticalIndicatorBehavior behavior) {
        return -behavior.getTopAndBottomOffset() + behavior.getParent().getHeight() > behavior.getConfiguration().getVisibleHeight();
    }
}
