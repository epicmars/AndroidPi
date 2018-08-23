package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

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

    /**
     * Minimum top and bottom offset of content view.
     */
    private int minOffset;

    /**
     * If set to true, default minimum offset will be {@link #headerVisibleHeight}.
     */
    private boolean useDefaultMinOffset = false;

    /**
     * The header's height.
     */
    protected int headerHeight;

    /**
     * The header's visible height.
     */
    protected int headerVisibleHeight;

    /**
     * The footer's maximum offset.
     */
    private int footerMaxOffset;

    /**
     * The footer's height.
     */
    private int footerHeight;

    /**
     * The footer's visible height. Maybe not very useful.
     */
    private int footerVisibleHeight = 0;

    private int defaultMinOffset;
    private boolean isFirstLayout = true;
    private boolean layoutNow = false;

    public ContentBehavior() {

    }

    public ContentBehavior(Context context) {
        super(context);
    }

    public ContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentBehavior, 0, 0);
        useDefaultMinOffset = a.getBoolean(R.styleable.ContentBehavior_lr_useDefaultMinOffset, false);
        minOffset = a.getDimensionPixelOffset(R.styleable.ContentBehavior_lr_minOffset, 0);
        defaultMinOffset = minOffset;
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (isFirstLayout || layoutNow) {
            // Compute max offset, it will not exceed parent height.
            maxOffset = Math.max(maxOffset, maxOffsetRatio * parent.getHeight());
            cancelAnimation();
            setTopAndBottomOffset(headerVisibleHeight);
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
        } else if (dy < 0){
            // When scrolling down, if footer is still visible.
            // xxx :
            if (child.getBottom() < coordinatorLayout.getHeight()) {
                int offset = MathUtils.clamp(-dy, 0, coordinatorLayout.getHeight() - child.getBottom());
                consumeOffset(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // If there is unconsumed pixels.
        // todo: For now only care about human touch.
        if (dyUnconsumed < 0) {
            // Scrolling down.
            // If top position of child can not scroll exceed maximum offset.
            if (child.getTop() >= maxOffset)
                return;
            int offset = MathUtils.clamp(-dyUnconsumed, 0, (int) maxOffset - child.getTop());
            if (offset != 0) {
                if (child.getTop() >= headerVisibleHeight) {
                    // When header is totally visible, do not consume none touch scroll,
                    // content can scroll to the maximum offset with the touch.
                    if (type != TYPE_TOUCH)
                        return;
                    consumeOffset(coordinatorLayout, child, offset, type, false);
                } else {
                    // Recompute the offset so that the top does not exceed headerVisibleHeight.
                    offset = MathUtils.clamp(-dyUnconsumed, 0, headerVisibleHeight - child.getTop());
                    consumeOffset(coordinatorLayout, child, offset, type, true);
                }
            }
        } else if (dyUnconsumed > 0) {
            // Scrolling up.
            // Can not scroll exceed footer maximum offset.
            int maxFooterTopBottomOffset = coordinatorLayout.getHeight() - footerMaxOffset;
            if (child.getBottom() <= maxFooterTopBottomOffset)
                return;
            int offset = MathUtils.clamp(-dyUnconsumed,  maxFooterTopBottomOffset - child.getBottom(),0);
            if (offset != 0) {
                consumeOffset(coordinatorLayout, child, offset, type, true);
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        for (ScrollListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, getTopAndBottomOffset(), (int) maxOffset, type == TYPE_TOUCH);
        }
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

    void resetMinOffset() {
        this.minOffset = defaultMinOffset;
    }

    void setMinOffset(int minOffset) {
        // todo: set to be minimum of defaultMinOffset and minOffset
        // this.minOffset = minOffset > defaultMinOffset ? defaultMinOffset : minOffset;
        this.minOffset = minOffset;
    }

    public void setFooterVisibleHeight(int footerVisibleHeight) {
        this.footerVisibleHeight = footerVisibleHeight;
    }

    public void setFooterMaxOffset(int footerMaxOffset) {
        this.footerMaxOffset = footerMaxOffset > footerHeight ? footerMaxOffset : footerHeight;
    }

    public void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public void setHeaderVisibleHeight(int headerVisibleHeight) {
        this.headerVisibleHeight = headerVisibleHeight;
        if (useDefaultMinOffset) {
            this.minOffset = headerVisibleHeight;
            this.defaultMinOffset = minOffset;
        }
        runWithView(new Runnable() {
            @Override
            public void run() {
                layoutNow = true;
                getChild().requestLayout();
            }
        });
    }
}
