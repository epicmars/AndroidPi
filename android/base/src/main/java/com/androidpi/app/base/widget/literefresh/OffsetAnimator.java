package com.androidpi.app.base.widget.literefresh;


/**
 * Created by jastrelax on 2018/8/8.
 */
public abstract class OffsetAnimator {

    public interface AnimationUpdateListener {
        void onAnimationUpdate(int value);
        void onAnimationEnd();
    }

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Cancel running animation.
     */
    public abstract void cancel();

    /**
     * Animate current offset to destination offset with a duration.
     * @param currentOffset  current offset
     * @param destOffset   destination offset
     * @param duration animation duration
     * @param listener animaiton listener
     */
    public abstract void animateOffsetWithDuration(int currentOffset, int destOffset, long duration,
                                                   AnimationUpdateListener listener);
}
