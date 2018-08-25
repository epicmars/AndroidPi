package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

/**
 * The header behavior consume some of the scrolled distance dispatch by the nested scrolling
 * content base on several rules.It will not consume the distance made by fling.
 * <p>
 * With some configuration, the view with which this behavior associated can move following
 * the nested scrolling content or fix in it's original position.
 * <p>
 * The content view can only nested with the visible part of header view.
 * <p>
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends VerticalBoundaryBehavior<V> {

    /**
     * Follow content view.
     */
    private static final int MODE_FOLLOW = 1;
    /**
     * Still, does not follow content view.
     */
    private static final int MODE_STILL = 0;

    /**
     * Follow when scroll down.
     */
    private static final int MODE_FOLLOW_DOWN = 2;

    /**
     * Follow when scroll up.
     */
    private static final int MODE_FOLLOW_UP = 3;

    private int mode = MODE_FOLLOW;
    private boolean isFirstLayout = true;

    {
        controller = new HeaderBehaviorController(this);
        addScrollListener(controller);
        runWithView(new Runnable() {
            @Override
            public void run() {
                controller.setDelegate(getContentBehavior().getController());
            }
        });
    }

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context) {
        this(context, null);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderBehavior, 0, 0);
        if (a.hasValue(R.styleable.HeaderBehavior_lr_headerMode)) {
            mode = a.getInt(R.styleable.HeaderBehavior_lr_headerMode, MODE_FOLLOW);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (isFirstLayout) {
            // Compute max offset, it will not exceed parent height.
            if (useDefaultMaxOffset) {
                // We want child can be fully visible by default.
                maxOffset = Math.max(GOLDEN_RATIO * parent.getHeight(), child.getHeight());
            } else {
                maxOffset = Math.max(maxOffset, maxOffsetRatio * parent.getHeight());
            }
            getContentBehavior().setHeaderVisibleHeight(getVisibleHeight());
            getContentBehavior().setHeaderHeight(child.getHeight());
            getContentBehavior().setMaxOffset(maxOffset);
            isFirstLayout = false;
        }
        return handled;
    }

    protected int computeOffsetOnDependentViewChanged(CoordinatorLayout parent, V child, View dependency, ContentBehavior contentBehavior) {
        // For now we don't care about invisible changes.
        if (contentBehavior.getTopAndBottomOffset() < 0)
            return 0;
        if (mode == MODE_FOLLOW_DOWN) {
            int totalHeight = child.getHeight() + dependency.getHeight();
            if (totalHeight <= parent.getHeight()) {
                // If view doesn't fill parent, content can not scroll up to the parent top.
                contentBehavior.setMinOffset(child.getHeight());
            } else {
                // Otherwise, we expect the content to be fully visible.
                contentBehavior.resetMinOffset();
            }
        }
        return contentBehavior.getTopAndBottomOffset() - child.getBottom();
    }

    @Override
    protected int transformOffsetCoordinate(int current, int height, int parentHeight) {
        return current + height;
    }

    protected float consumeOffsetOnDependentViewChanged(int currentOffset, int parentHeight, int height, int offset) {
        switch (mode) {
            case MODE_STILL:
                return 0;
            case MODE_FOLLOW_DOWN:
                if (offset < 0 && currentOffset <= 0) return 0;
                else return offset;
            case MODE_FOLLOW_UP:
                if (offset > 0 && currentOffset > 0) return 0;
                else if (offset < 0 && transformOffsetCoordinate(currentOffset, height, parentHeight) <= getContentBehavior().getMinOffset()) return 0;
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
    protected boolean isHiddenPartVisible() {
        return getTopAndBottomOffset() > -invisibleHeight;
    }

    /**
     * Tell if the visible part of header view is entirely visible.
     *
     * @return
     */
    protected boolean isVisible() {
        return getTopAndBottomOffset() >= -invisibleHeight;
    }

}
