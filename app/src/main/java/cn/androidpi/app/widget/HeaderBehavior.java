package cn.androidpi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends AnimationBehavior<V> {

    public interface HeaderListener {
        void onHide();

        void onShow();

        void onStopScroll();
    }

    private List<HeaderListener> mListeners = new ArrayList<>();
    private Scroller mScroller;
    private int mLastY;

    private int DEFAULT_HEIGHT;

    public HeaderBehavior() {
        this(null, null);
    }

    public HeaderBehavior(Context context) {
        this(context, null);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScroller = new Scroller(context);
    }

    public void addHeaderListener(HeaderListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removeHeaderListener(HeaderListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        super.onLayoutChild(parent, child, layoutDirection);
        if (DEFAULT_HEIGHT == 0) {
            DEFAULT_HEIGHT = child.getHeight();
        }
        cancelAnimation();
        setTopAndBottomOffset(-DEFAULT_HEIGHT);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // The action is pull along vertical axes.
        boolean started = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        //
        if (started) {
            cancelAnimation();
        }
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int top = child.getTop();
        int height = child.getHeight();
        // Scrolling may triggered by a fling, we only care about human touch.
        if (type == TYPE_TOUCH) {
            // If header is visible, it will consume the scroll range until it's invisible.
            if (isVisible()) {
                int offset = 0;
                if (dy > 0) {
                    // Pulling up.
                    offset = MathUtils.clamp(-dy, -(height + top), 0);
                } else if (dy < 0) {
                    // Pulling down.
                    offset = MathUtils.clamp(-dy, 0, -top);
                }
                if (offset != 0) {
                    offsetTopAndBottom(child, offset);
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
                    offsetTopAndBottom(child, offset);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        int top = child.getTop();
        int height = child.getHeight();
        if (type == TYPE_TOUCH) {
            if (isCompleteVisible()) {
                for (HeaderListener l : mListeners) {
                    l.onStopScroll();
                }
            } else if (isPartialVisible()) {
                int offset = - (height + top);
                animateOffsetDeltaTopAndBottom(coordinatorLayout, child, offset, 0);
            }
        }
    }

    private void animateOffsetDeltaTopAndBottom(CoordinatorLayout coordinatorLayout, final V child, int offset, int duration) {
        animateOffsetWithDuration(coordinatorLayout, child, getTopAndBottomOffset() + offset, duration);
    }

    private void offsetTopAndBottom(View child, int offset) {
        setTopAndBottomOffset(getTopAndBottomOffset() + offset);
    }

    private boolean isCompleteVisible() {
        return getTopAndBottomOffset() >= 0;
    }

    private boolean isPartialVisible() {
        int offset = getTopAndBottomOffset();
        return offset > -DEFAULT_HEIGHT && offset < 0;
    }

    private boolean isVisible() {
        return !isInvisible();
    }

    private boolean isInvisible() {
        return getTopAndBottomOffset() <= -DEFAULT_HEIGHT;
    }
}
