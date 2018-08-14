package com.androidpi.base.widget.literefresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by jastrelax on 2018/8/8.
 */
public class DefaultOffsetAnimator extends OffsetAnimator {

    private ValueAnimator mOffsetAnimator;

    public void animateOffsetWithDuration(int current, int offset, long duration, AnimationUpdateListener listener) {
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(new DecelerateInterpolator());
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
