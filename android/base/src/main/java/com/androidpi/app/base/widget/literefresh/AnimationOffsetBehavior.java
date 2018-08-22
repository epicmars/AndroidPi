package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by jastrelax on 2017/11/20.
 */

public class AnimationOffsetBehavior<V extends View> extends ViewOffsetBehavior<V> implements Handler.Callback {

    public interface ScrollListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);
    }

    private static final int MSG_VIEW_INITIATED = 1;

    public static final float GOLDEN_RATIO = 0.618F;

    protected List<ScrollListener> mListeners = new ArrayList<>();
    private OffsetAnimator offsetAnimator;
    protected CoordinatorLayout mParent;
    protected V mChild;
    protected float maxOffset = 0;
    private float maxOffsetRatio = GOLDEN_RATIO;
    protected int visibleHeight = 0;
    protected float invisibleHeight = 0;
    private float visibleHeightRatio = 0;
    protected int progressBase = 0;
    private Handler handler = new Handler(this);
    private Queue<Runnable> pendingActions = new LinkedList<>();

    public AnimationOffsetBehavior() {
    }

    public AnimationOffsetBehavior(Context context) {
        this(context, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OffsetBehavior, 0, 0);
        if (a.hasValue(R.styleable.OffsetBehavior_lr_maxOffsetRatio)) {
            maxOffsetRatio = a.getFloat(R.styleable.OffsetBehavior_lr_maxOffsetRatio, GOLDEN_RATIO);
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_maxOffset)) {
            maxOffset = a.getDimension(R.styleable.OffsetBehavior_lr_maxOffset, 0);
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeightRatio)) {
            visibleHeightRatio = a.getFloat(R.styleable.OffsetBehavior_lr_visibleHeightRatio, 0F);
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeight)) {
            visibleHeight = Math.round(a.getDimension(R.styleable.OffsetBehavior_lr_visibleHeight, 0));
        }
        a.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        if (mParent == null) {
            mParent = parent;
        }
        if (mChild == null) {
            mChild = child;
        }
        // Execute pending actions which need view to be initialized.
        handler.sendEmptyMessage(MSG_VIEW_INITIATED);
        // Compute visible height of child.
        visibleHeight = (int) Math.max((float) visibleHeight, visibleHeightRatio * child.getMeasuredHeight());
        invisibleHeight = child.getMeasuredHeight() - visibleHeight;
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        // Compute max offset, it will not exceed parent height.
        maxOffset = Math.max(maxOffset, maxOffsetRatio * parent.getHeight());
        return handled;
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
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
                    l.onScroll(getParent(), getChild(), progressBase + value, offset, (int) maxOffset, false);
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

    public int getVisibleHeight() {
        return visibleHeight;
    }

    public void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
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

    protected void runWithView(Runnable action) {
        if (getParent() == null || getChild() == null) {
            enqueuePendingActions(action);
        } else {
            runOnUiThread(action);
        }
    }

    protected void runOnUiThread(Runnable action) {
        if (handler == null) {
            handler = new Handler(this);
        }
        handler.post(action);
    }

    protected void enqueuePendingActions(Runnable action) {
        pendingActions.offer(action);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_VIEW_INITIATED:
                executePendingActions();
                break;
            default:
                break;
        }
        return true;
    }

    protected void executePendingActions() {
        Runnable action;
        while ((action = pendingActions.poll()) != null) {
            runOnUiThread(action);
        }
    }
}
