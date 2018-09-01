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

    {
        mode = MODE_FOLLOW;
    }

    @Override
    public int computeOffsetDeltaOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, CoordinatorLayout parent, View child, View dependency) {
        // For now we don't care about invisible changes.
        // And when content has reached minimum offset, we should not changed with it.
        // If content has reach it's minimum offset, header may have not changed yet.
        if (scrollingContentBehavior.isMinOffsetReached() && behavior.getChild().getBottom() <= scrollingContentBehavior.getConfiguration().getMinOffset())
            return 0;
        return scrollingContentBehavior.getTopAndBottomOffset() - child.getBottom();
    }

    @Override
    public int transformOffsetCoordinate(VerticalIndicatorBehavior behavior, int currentOffset, int height, int parentHeight) {
        return currentOffset + height;
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int parentHeight, int height, int currentOffset, int offsetDelta) {
        switch (mode) {
            case MODE_STILL:
                return 0;
            case MODE_FOLLOW_DOWN:
                if (offsetDelta < 0 && currentOffset <= 0) return 0;
                else return offsetDelta;
            case MODE_FOLLOW_UP:
                if (offsetDelta > 0 && currentOffset >= 0) {
                    return 0;
                }
                else if (offsetDelta < 0 && transformOffsetCoordinate(behavior, currentOffset, height, parentHeight) <= scrollingContentBehavior.getConfiguration().getMinOffset()) {
                    return 0;
                }
                else {
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
    public boolean isHiddenPartVisible(VerticalIndicatorBehavior behavior) {
        return behavior.getTopAndBottomOffset() > -behavior.getConfiguration().getInvisibleHeight();
    }
}
