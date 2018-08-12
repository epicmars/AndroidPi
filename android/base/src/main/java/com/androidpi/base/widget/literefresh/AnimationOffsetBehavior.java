package com.androidpi.base.widget.literefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/20.
 */

public class AnimationOffsetBehavior<V extends View> extends ViewOffsetBehavior<V> {


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
            }
        });

    }

}
