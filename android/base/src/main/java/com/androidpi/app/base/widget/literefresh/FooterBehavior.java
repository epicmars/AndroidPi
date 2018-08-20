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
 * Created by jastrelax on 2017/11/16.
 */

public class FooterBehavior<V extends View> extends AnimationOffsetBehavior<V> {

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
            BASE_LINE = parent.getBottom() - parent.getTop();
        }

        if (isFirstLayout) {
            cancelAnimation();
            setTopAndBottomOffset(BASE_LINE);
            isFirstLayout = false;
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (type == TYPE_TOUCH) {
            if (isVisible()) {
                int bottom = coordinatorLayout.getHeight();
                int top = child.getTop();
                int height = child.getHeight();

                int offset = 0;
                // Pull down
                if (dy < 0) {
                    offset = MathUtils.clamp(-dy, 0, bottom - top);
                }
                // Pull up
                else if (dy > 0) {
                    offset = MathUtils.clamp(-dy, -height + (bottom - top), 0);
                }
                if (offset != 0) {
                    for (ScrollListener l : mListeners) {
                        l.onStartScroll(coordinatorLayout, child, height, type == TYPE_TOUCH);
                    }
                    offsetTopAndBottom(coordinatorLayout, child, offset);
                    consumed[1] = -offset;
                }
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == TYPE_TOUCH) {
            if (isInvisible()) {
                int bottom = coordinatorLayout.getHeight();
                int top = child.getTop();
                int height = child.getHeight();
                // Pulling up unconsumed by scrolling content is consumed by footer.
                if (dyUnconsumed > 0) {
                    int offset = MathUtils.clamp(-dyUnconsumed, -height + (bottom - top), 0);
                    offsetTopAndBottom(coordinatorLayout, child, offset);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (type == TYPE_TOUCH) {
            for (ScrollListener l : mListeners) {
                l.onStopScroll(coordinatorLayout, child, BASE_LINE - getTopAndBottomOffset(), child.getHeight(), true);
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

    void stopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, boolean holdOn) {
        if (isVisible()) {
            if (holdOn) {
                child.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateOffsetWithDuration(coordinatorLayout, child, BASE_LINE, EXIT_DURATION);
                    }
                }, HOLD_ON_DURATION);
            } else {
                animateOffsetWithDuration(coordinatorLayout, child, BASE_LINE, EXIT_DURATION);
            }
        }
    }

    private void offsetTopAndBottom(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int offset) {
        setTopAndBottomOffset(getTopAndBottomOffset() + offset);
        for (ScrollListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, BASE_LINE - getTopAndBottomOffset() , -offset, child.getHeight(), true);
        }
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
