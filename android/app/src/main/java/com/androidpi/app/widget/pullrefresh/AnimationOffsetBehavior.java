package com.androidpi.app.widget.pullrefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by jastrelax on 2017/11/20.
 */

public class AnimationOffsetBehavior<V extends View> extends ViewOffsetBehavior<V> {

    public static interface AnimationUpdateListener {
        void onAnimationUpdate(int value);
    }

    private OffsetAnimator offsetAnimator = new SpringOffsetAnimator();

    public AnimationOffsetBehavior() {
        this(null, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void cancelAnimation() {
        offsetAnimator.cancel();
    }

    public void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, final V child, int offset, long duration) {
        int current = getTopAndBottomOffset();
        if (offset == current) {
            if (offsetAnimator != null && offsetAnimator.isRunning()) {
                offsetAnimator.cancel();
            }
            return;
        }
        offsetAnimator.animateOffsetWithDuration(coordinatorLayout, child, current, offset, duration, new AnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(int value) {
                setTopAndBottomOffset(value);
            }
        });

    }

}
