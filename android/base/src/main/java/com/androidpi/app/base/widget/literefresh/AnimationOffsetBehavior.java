package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/20.
 */

public class AnimationOffsetBehavior<V extends View> extends ViewOffsetBehavior<V> {

    public interface ScrollListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);
    }

    public static final float GOLDEN_RATIO = 0.618F;

    protected List<ScrollListener> mListeners = new ArrayList<>();
    private OffsetAnimator offsetAnimator;
    protected CoordinatorLayout mParent;
    protected V mChild;
    protected float maxOffset = GOLDEN_RATIO;
    private float maxOffsetRatio = GOLDEN_RATIO;
    protected float visibleHeight = 0;
    private float visibleHeightRatio = 0;

    public AnimationOffsetBehavior() {
        this(null, null);
    }

    public AnimationOffsetBehavior(Context context) {
        this(context, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderBehavior, 0, 0);
        if (a.hasValue(R.styleable.HeaderBehavior_lr_maxOffsetRatio)) {
            maxOffsetRatio = a.getFloat(R.styleable.HeaderBehavior_lr_maxOffsetRatio, GOLDEN_RATIO);
        }
        if (a.hasValue(R.styleable.HeaderBehavior_lr_maxOffset)) {
            maxOffset = a.getDimension(R.styleable.HeaderBehavior_lr_maxOffset, 0);
        }
        if (a.hasValue(R.styleable.HeaderBehavior_lr_visibleHeightRatio)) {
            visibleHeightRatio = a.getFloat(R.styleable.HeaderBehavior_lr_visibleHeightRatio, 0F);
        }
        if (a.hasValue(R.styleable.HeaderBehavior_lr_visibleHeight)) {
            visibleHeight = a.getDimension(R.styleable.HeaderBehavior_lr_visibleHeight, 0);
        }
        a.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);

        // Compute visible height of child.
        visibleHeight = Math.max(visibleHeight, visibleHeightRatio * child.getMeasuredHeight());

        return true;
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

        // Compute max offset, it will not exceed parent height.
        // If maxOffsetRatio is 0, will use child height instead.
        maxOffset = Math.max(maxOffset, maxOffsetRatio * parent.getHeight());
        maxOffset = Math.max(maxOffset, child.getHeight());
        return handled;
    }

    protected void cancelAnimation() {
        if (offsetAnimator != null && offsetAnimator.isRunning()) {
            offsetAnimator.cancel();
        }
    }

    public void animateOffsetWithDuration(CoordinatorLayout parent, View child, int offset, long duration) {
        int current = getTopAndBottomOffset();
        if (offset == current) {
            // No need to change offset.
            if (offsetAnimator != null && offsetAnimator.isRunning()) {
                offsetAnimator.cancel();
            }
            return;
        }
        if (offsetAnimator == null) {
            offsetAnimator = new SpringOffsetAnimator();
        } else {
            offsetAnimator.cancel();
        }
        offsetAnimator.animateOffsetWithDuration(current, offset, duration, new OffsetAnimator.AnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(int value) {
                boolean offsetChanged = setTopAndBottomOffset(value);
                if (!offsetChanged) {
                    parent.dispatchDependentViewsChanged(child);
                }
                for (ScrollListener l : mListeners) {
                    l.onScroll(getParent(), getChild(), getChild().getHeight() + value, offset, (int) maxOffset, false);
                }
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }

    public CoordinatorLayout getParent() {
        return mParent;
    }

    public V getChild() {
        return mChild;
    }

    public float getVisibleHeight() {
        return visibleHeight;
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
