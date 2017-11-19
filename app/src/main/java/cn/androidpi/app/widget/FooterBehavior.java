package cn.androidpi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class FooterBehavior<V extends View> extends ViewOffsetBehavior<V>{

    public interface FooterListener {
        void onHide();
        void onShow();
        void onStopScroll();
    }

    private int DEFAULT_HEIGHT;
    private int mTop;
    private int mHeight;

    private List<FooterListener> mListeners = new ArrayList<>();

    public FooterBehavior() {
        this(null, null);
    }

    public FooterBehavior(Context context) {
        this(context, null);
    }

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addFooterListener(FooterListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removeFooterListener(FooterListener listener) {
        if (null == listener)
            return;
        mListeners.remove(listener);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (DEFAULT_HEIGHT == 0) {
            mHeight = DEFAULT_HEIGHT = child.getHeight();
        }
        int offset = parent.getBottom();
        resetTop();
        setTopAndBottomOffset(offset);
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
            if (!isInvisible()) {
                int offset = 0;
                // Pull down
                if (dy < 0) {
                    offset = MathUtils.clamp(-dy, 0, -mTop);
                }
                // Pull up
                else if (dy > 0) {
                    offset = MathUtils.clamp(-dy, -mHeight-mTop, 0);
                }
                if (offset != 0) {
                    offsetTop(offset);
                    ViewCompat.offsetTopAndBottom(child, offset);
                    consumed[1] = -offset;
                }
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == TYPE_TOUCH) {
            if (isInvisible()) {
                // Pulling up unconsumed by scrolling content is consumed by footer.
                if (dyUnconsumed > 0) {
                    int offset = MathUtils.clamp(-dyUnconsumed, -mHeight-mTop, 0);
                    offsetTop(offset);
                    ViewCompat.offsetTopAndBottom(child, offset);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (type == TYPE_TOUCH) {
            if (isCompleteVisible()) {
                for (FooterListener l : mListeners) {
                    l.onStopScroll();
                }
            }
        }
    }

    private void resetTop() {
        mTop = 0;
    }

    private void offsetTop(int offset) {
        mTop += offset;
    }

    private boolean isCompleteVisible() {
        return mTop <= -DEFAULT_HEIGHT;
    }

    private boolean isPartialVisible() {
        return mTop > -DEFAULT_HEIGHT && mTop < 0;
    }

    private boolean isInvisible() {
        return mTop >= 0;
    }
}
