package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class FooterBehavior<V extends View> extends VerticalBoundaryBehavior<V> {

    private static final long EXIT_DURATION = 300L;
    private static final long HOLD_ON_DURATION = 1000L;

    private int DEFAULT_HEIGHT;
    private int BASE_LINE;

    private boolean isFirstLayout = true;

    public FooterBehavior() {
    }

    public FooterBehavior(Context context) {
        super(context);
    }

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (DEFAULT_HEIGHT == 0) {
            DEFAULT_HEIGHT = child.getHeight();
        }
        if (BASE_LINE == 0) {
            BASE_LINE = parent.getHeight();
        }

        if (isFirstLayout) {
            cancelAnimation();
            setTopAndBottomOffset(BASE_LINE);
            // Compute max offset, it will not exceed parent height.
            maxOffset = Math.max(maxOffset, child.getHeight());
            getContentBehavior().setFooterHeight(child.getHeight());
            getContentBehavior().setFooterMaxOffset((int) maxOffset);
            isFirstLayout = false;
        }
        return handled;
    }

    @Override
    protected int computeOffsetOnDependentViewChanged(CoordinatorLayout parent, V child, View dependency, ContentBehavior contentBehavior) {
        return contentBehavior.getTopAndBottomOffset() + dependency.getHeight() - child.getTop();
    }

    @Override
    protected int consumeOffsetOnDependentViewChanged(int current, int parentHeight, int offset) {
        return offset;
    }

    @Override
    protected int transformOffsetCoordinate(int current, int height, int parentHeight) {
        return -current + parentHeight;
    }

    private boolean isCompleteVisible() {
        return getTopAndBottomOffset() <= BASE_LINE - DEFAULT_HEIGHT;
    }

    private boolean isPartialVisible() {
        return getTopAndBottomOffset() > BASE_LINE - DEFAULT_HEIGHT && getTopAndBottomOffset() < BASE_LINE;
    }

    private boolean isInvisible() {
        return getTopAndBottomOffset() >= BASE_LINE;
    }

    private boolean isVisible() {
        return !isInvisible();
    }
}
