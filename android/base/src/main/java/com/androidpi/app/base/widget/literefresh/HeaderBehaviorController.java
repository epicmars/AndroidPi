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
        if (scrollingContentBehavior.getTopAndBottomOffset() < 0)
            return 0;
        if (mode == MODE_FOLLOW_DOWN) {
            int totalHeight = child.getHeight() + dependency.getHeight();
            if (totalHeight <= parent.getHeight()) {
                // If view doesn't fill parent, content can not scroll up to the parent top.
                scrollingContentBehavior.setMinOffset(child.getHeight());
            } else {
                // Otherwise, we expect the content to be fully visible.
                scrollingContentBehavior.resetMinOffset();
            }
        }
        return scrollingContentBehavior.getTopAndBottomOffset() - child.getBottom();
    }

    @Override
    public int transformOffsetCoordinate(VerticalIndicatorBehavior behavior, int current, int height, int parentHeight) {
        return current + height;
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int currentOffset, int parentHeight, int height, int offset) {
        switch (mode) {
            case MODE_STILL:
                return 0;
            case MODE_FOLLOW_DOWN:
                if (offset < 0 && currentOffset <= 0) return 0;
                else return offset;
            case MODE_FOLLOW_UP:
                if (offset > 0 && currentOffset > 0) return 0;
                else if (offset < 0 && transformOffsetCoordinate(behavior, currentOffset, height, parentHeight) <= scrollingContentBehavior.getMinOffset()) return 0;
                else return offset;
            case MODE_FOLLOW:
            default:
                return offset;
        }
    }

    /**
     * Tell if the hidden part of header view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * nt that case it's considered to be invisible.
     *
     * @return true if hidden part of header view is visible,
     * otherwise return false.
     */
    @Override
    public boolean isHiddenPartVisible(VerticalIndicatorBehavior behavior) {
        return behavior.getTopAndBottomOffset() > -behavior.invisibleHeight;
    }
}
