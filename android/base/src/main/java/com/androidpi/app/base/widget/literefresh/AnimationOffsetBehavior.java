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

public abstract class AnimationOffsetBehavior<V extends View, CTR extends BehaviorController> extends ViewOffsetBehavior<V> implements Handler.Callback {

    static final long HOLD_ON_DURATION = 500L;
    static final long SHOW_DURATION = 300L;
    static final long RESET_DURATION = 300L;

    interface ScrollingListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);
    }

    private static final int MSG_VIEW_INITIATED = 1;

    public static final float GOLDEN_RATIO = 0.618F;

    protected V mChild;
    protected CoordinatorLayout mParent;
    private OffsetAnimator offsetAnimator;
    protected int progressBase = 0;
    protected List<ScrollingListener> mListeners = new ArrayList<>();
    protected Handler handler = new Handler(this);
    private Queue<Runnable> pendingActions = new LinkedList<>();
    protected CTR controller;
    protected BehaviorConfiguration configuration;

    public AnimationOffsetBehavior(Context context) {
        this(context, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (configuration == null) {
            configuration = new BehaviorConfiguration();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OffsetBehavior, 0, 0);
        boolean hasMaxOffsetRatio = a.hasValue(R.styleable.OffsetBehavior_lr_maxOffsetRatio);
        boolean hasMaxOffset = a.hasValue(R.styleable.OffsetBehavior_lr_maxOffset);
        if (hasMaxOffsetRatio) {
            configuration.setMaxOffsetRatio(a.getFraction(R.styleable.OffsetBehavior_lr_maxOffsetRatio, 1, 1, 0f));
            configuration.setMaxOffsetRatioOfParent(a.getFraction(R.styleable.OffsetBehavior_lr_maxOffsetRatio, 1, 2, 0f));
        }
        if (hasMaxOffset) {
            configuration.setMaxOffset(a.getDimensionPixelOffset(R.styleable.OffsetBehavior_lr_maxOffset, 0));
        }
        // If maxOffset and maxOffsetRatio is not set then use default.
        configuration.setUseDefaultMaxOffset(!hasMaxOffsetRatio && !hasMaxOffset);
        configuration.setDefaultRefreshTriggerRange(context.getResources().getDimensionPixelOffset(R.dimen.defaultRefreshTriggerRange));
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
        return true;
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        pendingActions.clear();
        mListeners.clear();
        cancelAnimation();
    }

    protected void cancelAnimation() {
        if (offsetAnimator != null && offsetAnimator.isRunning()) {
            offsetAnimator.cancel();
        }
    }

    protected void animateOffsetDeltaWithDuration(CoordinatorLayout parent, View child, int offsetDelta, long duration) {
        animateOffsetWithDuration(parent, child, getTopAndBottomOffset() + offsetDelta, duration);
    }

    protected void animateOffsetWithDuration(CoordinatorLayout parent, View child, int offset, long duration) {
        int current = getTopAndBottomOffset();
        // No need to change offset.
        if (offset == current) {
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
                    //
                    parent.dispatchDependentViewsChanged(child);
                }
                for (ScrollingListener l : mListeners) {
                    l.onScroll(getParent(), getChild(), progressBase + value, offset, configuration.getMaxOffset(), false);
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

    protected void addScrollListener(ScrollingListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    protected void removeScrollListener(ScrollingListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }

    protected void runWithView(Runnable action) {
        if (action == null) return;
        if (getParent() == null || getChild() == null) {
            enqueuePendingActions(action);
        } else {
            runOnUiThread(action);
        }
    }

    protected void runOnUiThread(Runnable action) {
        if (action == null) return;
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

    public void requestLayout() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                getChild().requestLayout();
            }
        });
    }

    public CTR getController() {
        return controller;
    }

    public BehaviorConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BehaviorConfiguration configuration) {
        this.configuration = configuration;
        requestLayout();
    }
}
