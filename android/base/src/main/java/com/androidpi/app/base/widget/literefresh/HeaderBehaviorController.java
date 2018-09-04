package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/27.
 */
public class HeaderBehaviorController extends VerticalIndicatorBehaviorController {

    public HeaderBehaviorController(VerticalIndicatorBehavior behavior) {
        super(behavior);
    }

    @Override
    public int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent, View child, View dependency, VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior) {
        // For now we don't care about invisible changes.
        // And when content has reached minimum offset, we should not changed with it.
        // If content has reach it's minimum offset, header may have not changed yet.
        if (scrollingContentBehavior.isMinOffsetReached() && behavior.getChild().getBottom() + behavior.getConfiguration().getBottomMargin() <= scrollingContentBehavior.getConfiguration().getMinOffset())
            return 0;
        CoordinatorLayout.LayoutParams dependencyLp = ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams());
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        return dependency.getTop() - dependencyLp.topMargin - (child.getBottom() + lp.bottomMargin);
    }

    @Override
    public int transformOffsetCoordinate(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior, int currentOffset) {
        return currentOffset + child.getHeight();
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int currentOffset, int offsetDelta) {
        switch (mode) {
            case MODE_STILL:
                return 0;
            case MODE_FOLLOW_DOWN:
                if (offsetDelta < 0 && currentOffset <= 0) return 0;
                else return offsetDelta;
            case MODE_FOLLOW_UP:
                // If scroll down, or scroll up and the bottom has reach content's minimum offset.
                if ((offsetDelta > 0 && currentOffset >= 0)
                        || (offsetDelta < 0 && child.getBottom() + behavior.getConfiguration().getBottomMargin() <= scrollingContentBehavior.getConfiguration().getMinOffset())) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW:
            default:
                return offsetDelta;
        }
    }

    /**
     * Tell if the hidden part of header view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * in that case it's considered to be invisible.
     *
     * @return true if hidden part of header view is visible,
     * otherwise return false.
     */
    @Override
    public boolean isHiddenPartVisible(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior) {
        return behavior.getTopAndBottomOffset() > -behavior.getConfiguration().getInvisibleHeight();
    }
}
