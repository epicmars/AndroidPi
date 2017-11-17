package cn.androidpi.app.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
    }

    private int mState = STATE_IDLE;
    private List<HeaderListener> mListeners = new ArrayList<>();

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
        parent.onLayoutChild(child, layoutDirection);
        ViewCompat.offsetTopAndBottom(child, -child.getHeight());
        mState = STATE_HIDE;
        return true;
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
            // Unconsumed Pulling down is consumed by header.
            if (dyUnconsumed < 0) {
                offset = MathUtils.clamp(-dyUnconsumed, 0, -top);
            }
            // if dy > 0, pulling up is consumed by scrolling view.
            if (offset != 0) {
                ViewCompat.offsetTopAndBottom(child, offset);
                updateState(offset, top, child.getHeight());
            }
        }
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
