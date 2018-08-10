package com.androidpi.app.widget.pullrefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by jastrelax on 2018/8/8.
 */
public abstract class OffsetAnimator {

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public abstract void cancel();
    public abstract void animateOffsetWithDuration(CoordinatorLayout coordinatorLayout,
                                                   final View child,
                                                   int current,
                                                   int offset,
                                                   long duration,
                                                   AnimationOffsetBehavior.AnimationUpdateListener listener);
}
