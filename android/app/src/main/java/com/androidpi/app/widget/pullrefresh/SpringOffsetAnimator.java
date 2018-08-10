package com.androidpi.app.widget.pullrefresh;

import android.support.animation.DynamicAnimation;
import android.support.animation.FloatValueHolder;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import static android.support.animation.SpringForce.DAMPING_RATIO_NO_BOUNCY;

/**
 * Created by jastrelax on 2018/8/8.
 */
public class SpringOffsetAnimator extends OffsetAnimator {

    private SpringAnimation springAnimation;

    @Override
    public void cancel() {
        if (null != springAnimation && springAnimation.isRunning()) {
            springAnimation.cancel();
        }
    }

    @Override
    public void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout, View child, int current, int offset, long duration, AnimationOffsetBehavior.AnimationUpdateListener listener) {
        if (null == springAnimation) {
            springAnimation = new SpringAnimation(new FloatValueHolder());
        } else {
            springAnimation.cancel();
        }
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if (listener == null)
                    return;
                listener.onAnimationUpdate((int) value);
            }
        });

        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                setRunning(false);
            }
        });

        SpringForce springForce = new SpringForce()
                .setDampingRatio(DAMPING_RATIO_NO_BOUNCY)
                .setFinalPosition(offset);

        springAnimation
                .setSpring(springForce)
                .setStartValue(current);

        springAnimation.start();
        setRunning(true);
    }
}
