package com.androidpi.app.widget.pullrefresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by jastrelax on 2018/8/8.
 */
public class DefaultOffsetAnimator extends OffsetAnimator {

    private ValueAnimator mOffsetAnimator;

    public void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout,
                                          final View child,
                                          int current,
                                          int offset,
                                          long duration,
                                          AnimationOffsetBehavior.AnimationUpdateListener listener) {
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (listener == null)
                        return;
                    listener.onAnimationUpdate((int) animation.getAnimatedValue());
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(duration);
        mOffsetAnimator.setIntValues(current, offset);
        mOffsetAnimator.start();
        mOffsetAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setRunning(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setRunning(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setRunning(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void cancel() {
        if (mOffsetAnimator != null) {
            mOffsetAnimator.cancel();
        }
    }
}
