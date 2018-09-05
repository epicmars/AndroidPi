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
    public int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                        View dependency,
                                                        VerticalIndicatorBehavior behavior,
                                                        ScrollingContentBehavior contentBehavior) {
        CoordinatorLayout.LayoutParams lpDependency = ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams());
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        return dependency.getBottom() + lpDependency.bottomMargin - (child.getTop() - lp.topMargin);
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                     VerticalIndicatorBehavior behavior,
                                                     ScrollingContentBehavior contentBehavior,
                                                     int currentOffset, int offsetDelta) {
        switch (mode) {
            case MODE_STILL:
                if (child.getTop() - behavior.getConfiguration().getTopMargin() ==
                        -behavior.getConfiguration().getInitialVisibleHeight() + parent.getHeight()) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW_DOWN:
                // If scrolling up and current offset has reach the initial visible height, don't follow.
                if (offsetDelta < 0
                        && child.getTop() - behavior.getConfiguration().getTopMargin()
                        <= -behavior.getConfiguration().getInitialVisibleHeight() + parent.getHeight()) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW_UP:
                // If scrolling down and current offset has reach the initial visible height, don't follow..
                if (offsetDelta > 0
                        && child.getTop() - behavior.getConfiguration().getTopMargin()
                        >= -behavior.getConfiguration().getInitialVisibleHeight() + parent.getHeight()) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW:
            default:
                return offsetDelta;
        }
    }

    @Override
    public int transformOffsetCoordinate(CoordinatorLayout parent, View child,
                                         VerticalIndicatorBehavior behavior,
                                         int currentOffset) {
        // The current offset is the footer's top and bottom offset.
        return -currentOffset + parent.getHeight();
    }

    @Override
    public boolean isHiddenPartVisible(CoordinatorLayout parent, View child,
                                       VerticalIndicatorBehavior behavior) {
        return -(child.getTop() - behavior.getConfiguration().getTopMargin()) + behavior.getParent().getHeight()
                > behavior.getConfiguration().getInitialVisibleHeight();
    }
}
