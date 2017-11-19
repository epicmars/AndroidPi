package cn.androidpi.app.widget;

import android.animation.ValueAnimator;
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

import timber.log.Timber;

import static android.support.v4.view.ViewCompat.TYPE_NON_TOUCH;
import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class HeaderBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final int STATE_IDLE = 0;
    private static final int STATE_HIDE = 1;
    private static final int STATE_OVERLAP = 2;
    private static final int STATE_SHOW = 3;

    public interface HeaderListener {
        void onHide();

        void onShow();

        void onStopScroll();
    }

    private int mState = STATE_IDLE;
    private List<HeaderListener> mListeners = new ArrayList<>();

    private Scroller mScroller;
    private int mLastY;

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context) {
        this.mScroller = new Scroller(context);
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

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
        // First let parent lay it out.
        parent.onLayoutChild(child, layoutDirection);
        Timber.d("onLayoutChild() called with: parent = [" + parent + "], child = [" + child + "], layoutDirection = [" + layoutDirection + "]");
        int offset = child.getBottom();
        // If current state is STATE_HIDE, reset header silently.
        if (mState == STATE_HIDE) {
            ViewCompat.offsetTopAndBottom(child, -offset);
        } else {
            // If current state is not STATE_HIDE then scroll to the top position.
            // Reset header with animation.
            animateOffset(child, offset);
        }
        mState = STATE_HIDE;
        return true;
    }

    private void animateOffset(V child, int offset) {
        mLastY = 0;
        mScroller.startScroll(0, 0, 0, offset);
        animateStep(child, ValueAnimator.getFrameDelay());
    }

    private void animateStep(final V child, final long timeDelta) {
        if (!mScroller.computeScrollOffset()) {
            return;
        }
        int current = mScroller.getCurrY();
        ViewCompat.offsetTopAndBottom(child, -(current - mLastY));
        mLastY = current;
        child.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateStep(child, timeDelta);
            }
        }, timeDelta);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // the action is pull along vertical axes
        if ((axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int top = child.getTop();
        int height = child.getHeight();
        int offset = 0;
        if (mState == STATE_OVERLAP) {
            // pull down
            if (dy < 0) {
                offset = MathUtils.clamp(-dy, 0, -top);
            }
            // pull up
            else if (dy > 0) {
                offset = MathUtils.clamp(-dy, -(height + top), 0);
            }
        } else if (mState == STATE_SHOW) {
            // pull up
            if (dy > 0) {
                offset = MathUtils.clamp(-dy, -(height + top), 0);
            }
            // if dy < 0, pulling down is not consumed
        }
        if (offset != 0) {
            ViewCompat.offsetTopAndBottom(child, offset);
            updateState(offset, top, height);
            consumed[1] = -offset;
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        int offset = 0;
        int top = child.getTop();
        if (mState == STATE_HIDE) {
            // If scrolling started by a fling, unconsumed pulling down is not consumed by header,
            // otherwise consume it.
            if (TYPE_NON_TOUCH == type)
                return;
            if (dyUnconsumed < 0) {
                offset = MathUtils.clamp(-dyUnconsumed, 0, -top);
            }
            if (offset != 0) {
                ViewCompat.offsetTopAndBottom(child, offset);
                updateState(offset, top, child.getHeight());
            }
            // if dy > 0, pulling up is consumed by scrolling view.
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (type == TYPE_TOUCH) {
            if (mState == STATE_SHOW) {
                for (HeaderListener l : mListeners) {
                    l.onStopScroll();
                }
            }
        }
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        Timber.d("onNestedPreFling() called with: coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
        // If header is hidden and scrolling content has reach the top, then consume the fling.
        boolean consumed = mState == STATE_OVERLAP;
        return consumed;
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Timber.d("onNestedFling() called with: coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "], consumed = [" + consumed + "]");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private void updateState(int offset, int top, int height) {
        int newTop = top + offset;
        if (newTop < 0 && newTop > -height) {
            mState = STATE_OVERLAP;
        } else if (newTop == 0) {
            mState = STATE_SHOW;
            for (HeaderListener l : mListeners) {
                l.onShow();
            }
        } else if (newTop == -height) {
            mState = STATE_HIDE;
            for (HeaderListener l : mListeners) {
                l.onHide();
            }
        }
    }
}
