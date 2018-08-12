package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.base.R;

import timber.log.Timber;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private int childHeight;
    private int parentHeight;
    private boolean isFirstLayout = true;

    public HeaderBehavior(Context context) {
        this(context, null);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderBehavior, 0, 0);
        if (a.hasValue(R.styleable.HeaderBehavior_maxOffset)) {
            maxOffset = a.getFloat(R.styleable.HeaderBehavior_maxOffset, 0.618f);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        childHeight = child.getHeight();
        parentHeight = parent.getHeight();
        // Relayout should not change current offset.
        if (isFirstLayout) {
            cancelAnimation();
            setTopAndBottomOffset(-childHeight);
            isFirstLayout = false;
        }

        // Compute max offset.
        if (maxOffset <= 1f) {
            maxOffset *= parent.getHeight();
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // The action is pull along vertical axes.
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (started) {
            cancelAnimation();
            for (ScrollListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, (int) maxOffset);
            }
        }
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int bottom = child.getBottom();
        // Height of child may have changed.
        float maxOffset = Math.max(this.maxOffset, child.getHeight());
        // Scrolling may triggered by a fling, we only care about human touch.
        if (type == TYPE_TOUCH) {
            // If header is visible, it will consume the scroll range until it's invisible.
            if (isVisible()) {
                float offset = 0;
                if (dy > 0) {
                    // Pulling up.
                    offset = MathUtils.clamp(-dy, -bottom, 0);
                } else if (dy < 0) {
                    // Pulling down.
                    offset = MathUtils.clamp(-dy, 0, maxOffset - bottom);
                }
                if (offset != 0) {
                    for (ScrollListener l : mListeners) {
                        l.onPreScroll(coordinatorLayout, child, (int) maxOffset);
                    }
                    consumeOffset(coordinatorLayout, child, (int) offset);
                    consumed[1] = - (int) offset;
                }
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        int top = child.getTop();
        if (type == TYPE_TOUCH) {
            // If header is invisible and the scrolling content has reached it's top,
            // The pulling down range not consumed by the scrolling view is consumed by the header.
            if (isInvisible()) {
                if (dyUnconsumed < 0) {
                    int offset = MathUtils.clamp(-dyUnconsumed, 0, -top);
                    consumeOffset(coordinatorLayout, child, offset);
                }
            }
        }
    }

    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offset) {
        int current = getTopAndBottomOffset();
        int consumed = onConsumeOffset(current, coordinatorLayout.getHeight(), offset);
        current += consumed;
        setTopAndBottomOffset(current);
        for (ScrollListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, child.getHeight() + current, offset, (int) maxOffset, true);
        }
        return consumed;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        // If header is visible, consume the fling.
        if (isVisible()) {
            return true;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (type == TYPE_TOUCH) {
            int height = child.getHeight();
            for (ScrollListener l : mListeners) {
                l.onStopScroll(coordinatorLayout, child, height + getTopAndBottomOffset(), (int) maxOffset);
            }
        }
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }

    protected int onConsumeOffset(int current, int parentHeight, int offset) {
        return offset;
    }

    private boolean isCompleteVisible() {
        return getTopAndBottomOffset() >= 0;
    }

    private boolean isPartialVisible() {
        int offset = getTopAndBottomOffset();
        return offset > -childHeight && offset < 0;
    }

    protected boolean isVisible() {
        return !isInvisible();
    }

    protected boolean isInvisible() {
        return getTopAndBottomOffset() <= -childHeight;
    }
}
