package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

import java.util.List;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * Super class of header and footer behavior.
 * <br>
 * The header and footer behaviors are almost the same, the main difference is that
 * they have different coordinate system when we trace the bottom and top position
 * of the view to which they attached respectively.
 * view.
 * <br>
 * As we have record the bottom and top offset of the view using the view's default coordinate
 * system, whose original point is the left top point of the parent view. Relative to that original
 * point the right and bottom position is positive.
 *
 * <br>
 * Now we need to trace how much the header has scroll from the top of the parent view.
 * We need to transform the bottom position of the view to which the header behavior is attached
 * in the coordinate system of the parent view to another one. This coordinate system would be a
 * affine matrix transformation below:
 *
 * <pre>
 *      |1 0 height||x|   |         x|
 *      |0 1 height||y| = |y + height|
 *      |0 0      1||1|   |         1|
 * </pre>
 *
 * And we also need to trace how much the footer view has scrolled from the bottom of the parent
 * view, We use top position of the view as the traced point, the coordinate system would be a
 * affine transformation below:
 *
 * <pre>
 *      |1   0              0||x|   |                x|
 *      |0   -1  parentHeight||y| = |-y + parentHeight|
 *      |0   0              1||1|   |                1|
 * </pre>
 *
 * Created by jastrelax on 2018/8/23.
 */
public abstract class VerticalBoundaryBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    protected int visibleHeight = 0;
    protected float invisibleHeight = 0;
    private float visibleHeightRatio = 0;

    {
        controller = new VerticalBoundaryBehaviorController(this);
        addScrollListener(controller);
        runWithView(new Runnable() {
            @Override
            public void run() {
                controller.setDelegate(getContentBehavior().getController());
            }
        });
    }

    public VerticalBoundaryBehavior() {
    }

    public VerticalBoundaryBehavior(Context context) {
        this(context, null);
    }

    public VerticalBoundaryBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OffsetBehavior, 0, 0);
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
        boolean handled = super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        // Compute visible height of child.
        visibleHeight = (int) Math.max((float) visibleHeight, visibleHeightRatio * child.getMeasuredHeight());
        invisibleHeight = child.getMeasuredHeight() - visibleHeight;
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, child.getHeight(), type == TYPE_TOUCH);
            }
        }
        return start;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        // If header should be hidden entirely, and hidden part is visible now consume the fling.
        // Otherwise, do nothing.
        if (isHiddenPartVisible() && visibleHeight == 0) {
            return true;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        int height = child.getHeight();
        int parentHeight = coordinatorLayout.getHeight();
        for (ScrollListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, transformOffsetCoordinate(getTopAndBottomOffset(), height, parentHeight), height, type == TYPE_TOUCH);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ContentBehavior;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        int offset = 0;
        if (behavior instanceof ContentBehavior) {
            ContentBehavior contentBehavior = (ContentBehavior) behavior;
            offset = computeOffsetOnDependentViewChanged(parent, child, dependency, contentBehavior);
        }
        if (offset != 0) {
            // todo: use TYPE_TOUCH or not
            consumeOffsetOnDependentViewChanged(parent, child, offset, TYPE_TOUCH);
            return true;
        }
        return false;
    }

    private int consumeOffsetOnDependentViewChanged(CoordinatorLayout coordinatorLayout, View child, int offset, int type) {
        int currentOffset = getTopAndBottomOffset();
        int parentHeight = coordinatorLayout.getHeight();
        int height = child.getHeight();
        // Before child consume the offset.
        for (ScrollListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, transformOffsetCoordinate(currentOffset, height, parentHeight), height, type == TYPE_TOUCH);
        }
        int consumed = consumeOffsetOnDependentViewChanged(currentOffset, parentHeight, height, offset);
        currentOffset += consumed;
        setTopAndBottomOffset(currentOffset);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's onNestedScroll().
        // The header view itself can make some transformation by setTranslationY() that may keep it's drawing rectangle.
        // Such as when scroll down, use setTranslationY() with negative value.
        // In this case CoordinatorLayout will not call onDependentViewChanged().
        // So We need to call onDependentViewChanged() manually.
        // coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, transformOffsetCoordinate(currentOffset, height, parentHeight), offset, height, type == TYPE_TOUCH);
        }
        return consumed;
    }

    protected abstract int computeOffsetOnDependentViewChanged(CoordinatorLayout parent, V child, View dependency, ContentBehavior contentBehavior);

    protected abstract int consumeOffsetOnDependentViewChanged(int currentOffset, int parentHeight, int height, int offset);

    protected abstract int transformOffsetCoordinate(int current, int height, int parentHeight);

    private View findDependencyChild(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ContentBehavior) {
                return v;
            }
        }
        return null;
    }

    private ContentBehavior findDependencyBehavior(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ContentBehavior) {
                return (ContentBehavior) p.getBehavior();
            }
        }
        return null;
    }

    public ContentBehavior getContentBehavior() {
        return findDependencyBehavior(getParent(), getChild());
    }

    public View getContentChild() {
        return findDependencyChild(getParent(), getChild());
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        cancelAnimation();
        mListeners.clear();
        mParent = null;
        mChild = null;
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }

    public void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
    }

    /**
     * Tell if the hidden part of the view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * in that case it's considered to be invisible.
     *
     * @return true if hidden part of view is visible,
     * otherwise return false.
     */
    protected abstract boolean isHiddenPartVisible();
}
