package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends AnimationOffsetBehavior<V> {


    public interface HeaderListener {

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max);
    }

    private List<HeaderListener> mListeners = new ArrayList<>();
    private CoordinatorLayout mParent;
    private V mChild;

    private int childHeight;
    private boolean isFirstLayout = true;

    public HeaderBehavior() {
        this(null, null);
    }

    public HeaderBehavior(Context context) {
        this(context, null);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void addHeaderListener(HeaderListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    protected void removeHeaderListener(HeaderListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (mParent == null) {
            mParent = parent;
        }
        if (mChild == null) {
            mChild = child;
        }
        if (childHeight == 0) {
            childHeight = child.getHeight();
        }
        // Relayout should not change current offset.
        if (isFirstLayout) {
            cancelAnimation();
            setTopAndBottomOffset(-childHeight);
            isFirstLayout = false;
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // The action is pull along vertical axes.
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (started) {
            cancelAnimation();
        }
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int bottom = child.getBottom();
        int parentHeight = coordinatorLayout.getHeight();
        // Scrolling may triggered by a fling, we only care about human touch.
        if (type == TYPE_TOUCH) {
            // If header is visible, it will consume the scroll range until it's invisible.
            if (isVisible()) {
                int offset = 0;
                if (dy > 0) {
                    // Pulling up.
                    offset = MathUtils.clamp(-dy, -bottom, 0);
                } else if (dy < 0) {
                    // Pulling down.
                    offset = MathUtils.clamp(-dy, 0, parentHeight - bottom);
                }
                if (offset != 0) {
                    for (HeaderListener l : mListeners) {
                        l.onPreScroll(coordinatorLayout, child, parentHeight);
                    }
                    consumeOffset(coordinatorLayout, child, offset);
                    consumed[1] = -offset;
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
            for (HeaderListener l : mListeners) {
                l.onStopScroll(coordinatorLayout, child, height + getTopAndBottomOffset(), height);
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

    public CoordinatorLayout getParent() {
        return mParent;
    }

    public V getChild() {
        return mChild;
    }

    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offset) {
        int current = getTopAndBottomOffset();
        int height = child.getHeight();
        int parentHeight = coordinatorLayout.getHeight();
        int consumed = onConsumeOffset(current, parentHeight, offset);
        Timber.d("%d %d %d %d", parentHeight, current, offset, consumed);
        current += consumed;
        setTopAndBottomOffset(current);
        for (HeaderListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, height + current, offset, height);
        }
        return consumed;
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
