package com.androidpi.app.widget.pullrefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/20.
 */

public class AnimationBehavior<V extends View> extends ViewOffsetBehavior<V> {

    private ValueAnimator mOffsetAnimator;

    public AnimationBehavior() {
        this(null, null);
    }

    public AnimationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void cancelAnimation() {
        if (mOffsetAnimator != null) {
            mOffsetAnimator.cancel();
        }
    }

    public void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, final V child, int offset, int duration) {
        int current = getTopAndBottomOffset();
        if (offset == current) {
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
                mOffsetAnimator.cancel();
            }
            return;
        }
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(new ViscousFluidInterpolator());
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setTopAndBottomOffset((int)animation.getAnimatedValue());
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
//        mOffsetAnimator.setDuration(duration);
        mOffsetAnimator.setIntValues(current, offset);
        mOffsetAnimator.start();
    }
}
