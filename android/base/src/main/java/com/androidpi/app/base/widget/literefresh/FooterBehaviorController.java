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
    public int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent, View child, View dependency, VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior) {
        CoordinatorLayout.LayoutParams lpDependency = ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams());
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        return dependency.getBottom() + lpDependency.bottomMargin - (child.getTop() - lp.topMargin);
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior, ScrollingContentBehavior scrollingContentBehavior, int currentOffset, int offsetDelta) {
        switch (mode) {
            case MODE_STILL:
                return 0;
//            case MODE_FOLLOW_DOWN:
//                // scroll up and current offset has reach the
//                if (offsetDelta < 0 && currentOffset)
//
//            case MODE_FOLLOW:
            default:
                return offsetDelta;
        }
    }

    @Override
    public int transformOffsetCoordinate(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior, int currentOffset) {
        // The current offset is the footer's top and bottom offset.
        return -currentOffset + parent.getHeight();
    }

    @Override
    public boolean isHiddenPartVisible(CoordinatorLayout parent, View child, VerticalIndicatorBehavior behavior) {
        return -behavior.getTopAndBottomOffset() + behavior.getParent().getHeight() > behavior.getConfiguration().getVisibleHeight();
    }
}
