package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * A behavior for nested scrollable child of {@link CoordinatorLayout}.
 * <p>
 * It's attach to the nested scrolling target view, such as {@link android.support.v4.widget.NestedScrollView},
 * {@link android.support.v7.widget.RecyclerView} which implement {@link android.support.v4.view.NestedScrollingChild}.
 * <p>
 * <p>
 * View with this behavior must be a direct child of {@link CoordinatorLayout}.
 * <p>
 * <p>
 * Created by jastrelax on 2018/8/21.
 */
public class ContentBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private boolean isFirstLayout = true;
    private boolean layoutNow = false;
    // Minimum offset that content can scroll up.
    private int minOffset;
    // The header's height.
    protected int headerHeight = 0;

    public ContentBehavior() {

    }

    public ContentBehavior(Context context) {
        super(context);
    }

    public ContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (isFirstLayout || layoutNow) {
            cancelAnimation();
            setTopAndBottomOffset(minOffset);
            isFirstLayout = false;
            layoutNow = false;
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, (int) maxOffset, type == TYPE_TOUCH);
            }
        }
        return start;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0) {
            // When scrolling up, compute the top offset which content can reach.
            int topOffset = minOffset;
            if (child.getTop() <= topOffset)
                return;
            int offset = MathUtils.clamp(-dy, topOffset - child.getTop(), 0);
            if (offset != 0) {
                consumeOffset(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // When scrolling down, if there is unconsumed pixels.
        // Make use of them to move content view to reach maximum offset.
        // todo: For now only care about human touch.
        if (dyUnconsumed < 0) {
            if (child.getTop() >= maxOffset)
                return;
            int offset = MathUtils.clamp(-dyUnconsumed, 0, (int) maxOffset - child.getTop());
            if (offset != 0) {
                if (child.getTop() >= visibleHeight) {
                    if (type != TYPE_TOUCH)
                        return;
                    consumeOffset(coordinatorLayout, child, offset, type, false);
                } else {
                    // Recompute the offset so that the top does not exceed visibleHeight.
                    offset = MathUtils.clamp(-dyUnconsumed, 0, visibleHeight - child.getTop());
                    consumeOffset(coordinatorLayout, child, offset, type, true);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        for (ScrollListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, getTopAndBottomOffset(), (int) maxOffset, type == TYPE_TOUCH);
        }
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        cancelAnimation();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }

    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offset, int type, boolean comsumeRawOffset) {
        int current = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, current, (int) maxOffset, type == TYPE_TOUCH);
        }
        int consumed = comsumeRawOffset ? offset : onConsumeOffset(current, coordinatorLayout.getHeight(), offset);
        current += consumed;
        setTopAndBottomOffset(current);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's onNestedScroll().
        // The header view itself can make some transformation by setTranslationY() that may keep it's drawing rectangle.
        // In this case CoordinatorLayout will not call onDependentViewChanged().
        // So We need to call onDependentViewChanged() manually.
        coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, current, offset, (int) maxOffset, type == TYPE_TOUCH);
        }
        return consumed;
    }

    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        return offset;
    }

    @Override
    public void setVisibleHeight(int visibleHeight) {
        super.setVisibleHeight(visibleHeight);
        minOffset = 0;
        layoutNow = true;
        getChild().requestLayout();
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }
}
