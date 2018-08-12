package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/20.
 */

public class AnimationOffsetBehavior<V extends View> extends ViewOffsetBehavior<V> {

    public interface ScrollListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max);
    }

    public static final float GOLDEN_RATIO = 0.618f;

    protected List<ScrollListener> mListeners = new ArrayList<>();
    private OffsetAnimator offsetAnimator = new SpringOffsetAnimator();
    protected CoordinatorLayout mParent;
    protected V mChild;
    protected float maxOffset = GOLDEN_RATIO;

    public AnimationOffsetBehavior() {
        this(null, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        if (mParent == null) {
            mParent = parent;
        }
        if (mChild == null) {
            mChild = child;
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    protected void cancelAnimation() {
        offsetAnimator.cancel();
    }

    public void animateOffsetWithDuration(int offset, long duration) {
        int current = getTopAndBottomOffset();
        if (offset == current) {
            if (offsetAnimator != null && offsetAnimator.isRunning()) {
                offsetAnimator.cancel();
            }
            return;
        }
        offsetAnimator.animateOffsetWithDuration(current, offset, duration, new OffsetAnimator.AnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(int value) {
                setTopAndBottomOffset(value);
                for (ScrollListener l : mListeners) {
                    l.onScroll(getParent(), getChild(), getChild().getHeight() + value, offset, (int) maxOffset, false);
                }
            }
        });
    }

    public CoordinatorLayout getParent() {
        return mParent;
    }

    public V getChild() {
        return mChild;
    }

    protected void addScrollListener(ScrollListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    protected void removeScrollListener(ScrollListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }
}
