package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

import timber.log.Timber;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * A behavior for nested scrollable child of {@link CoordinatorLayout}.
 * <p>
 * It's attach to the nested scrolling target view, such as {@link android.support.v4.widget.NestedScrollView},
 * {@link android.support.v7.widget.RecyclerView} which implement {@link android.support.v4.view.NestedScrollingChild}.
 * <p>
 * View with this behavior must be a direct child of {@link CoordinatorLayout}.
 * <p>
 * Created by jastrelax on 2018/8/21.
 */
public class ScrollingContentBehavior<V extends View> extends AnimationOffsetBehavior<V, ContentBehaviorController> {

    private BehaviorConfiguration headerConfig;
    private BehaviorConfiguration footerConfig;

    {
        controller = new ContentBehaviorController(this);
        addScrollListener(controller);
    }

    public ScrollingContentBehavior(Context context) {
        this(context, null);
    }

    public ScrollingContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (headerConfig == null) {
            headerConfig = createIndicatorConfig();
        }
        if (footerConfig == null) {
            footerConfig = createIndicatorConfig();
            footerConfig.setUseDefaultMaxOffset(true);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentBehavior, 0, 0);
        boolean hasMinOffset = a.hasValue(R.styleable.ContentBehavior_lr_minOffset);
        if (hasMinOffset) {
            configuration.setMinOffset(a.getDimensionPixelOffset(R.styleable.ContentBehavior_lr_minOffset, 0));
            configuration.setCachedMinOffset(configuration.getMinOffset());
        }

        boolean hasMinOffsetRatio = a.hasValue(R.styleable.ContentBehavior_lr_minOffsetRatio);
        if (hasMinOffsetRatio) {
            configuration.setMinOffsetRatio(a.getFraction(R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 1, 0f));
            configuration.setMinOffsetRatioOfParent(a.getFraction(R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 2, 0f));
        }
        if (!hasMinOffset && !hasMinOffsetRatio) {
            configuration.setUseDefaultMinOffset(true);
        }
        if (a.hasValue(R.styleable.ContentBehavior_lr_headerVisibleHeight)) {
            headerConfig.setVisibleHeight(a.getDimensionPixelOffset(R.styleable.ContentBehavior_lr_headerVisibleHeight, 0));
        }
        a.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        boolean handled = super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        if (headerConfig.getVisibleHeight() < 0 || configuration.getMinOffset() > 0) {
            // The minimum offset is used to limit the content view offset.
            // Besides, the minimum offset and header's visible height are used to reset content.
            // We must make sure after resetting, either it's top reaches the minimum offset or
            // header visible height, it depends on which one is larger.Now minimum offset is always
            // less than or equal to header visible height.
            int height = child.getMeasuredHeight();
            if (headerConfig.getVisibleHeight() < 0) {
                height -= headerConfig.getVisibleHeight();
            }
            if (configuration.getMinOffset() > 0) {
                height -= configuration.getMinOffset();
            }
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightSpec, heightUsed);
        }
        return handled;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (!configuration.isSettled() || !headerConfig.isSettled() || !footerConfig.isSettled()) {
            // Compute content view's minimum offset.
            // Default minimum offset is zero.
            if (!configuration.isUseDefaultMinOffset()) {
                configuration.setMinOffset((int) Math.max(configuration.getMinOffset(), configuration.getMinOffsetRatioOfParent() > configuration.getMinOffsetRatio() ? configuration.getMinOffsetRatio() * parent.getHeight() : configuration.getMinOffsetRatio() * child.getHeight()));
                configuration.setCachedMinOffset(configuration.getMinOffset());
            }

            configuration.setMinOffset(Math.min(configuration.getMinOffset(), headerConfig.getVisibleHeight()));
            if (configuration.getMinOffset() > 0) child.requestLayout();

            // Compute max offset, it will not exceed parent height.
            if (configuration.isUseDefaultMaxOffset()) {
                configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(), GOLDEN_RATIO * parent.getHeight()));
                // If header has set a maximum offset and it's positive, then use it.
                if (!headerConfig.isUseDefaultMaxOffset() && headerConfig.getMaxOffset() > 0) {
                    configuration.setMaxOffset(headerConfig.getMaxOffset());
                }
            } else {
                configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(), configuration.getMaxOffsetRatioOfParent() > configuration.getMaxOffsetRatio() ? configuration.getMaxOffsetRatio() * parent.getHeight() : configuration.getMaxOffsetRatio() * child.getHeight()));
            }

            // If footer's max offset is not settled yet.
            if (footerConfig.isUseDefaultMaxOffset() && footerConfig.getMaxOffset() <= 0) {
                footerConfig.setMaxOffset((int) ((1 - GOLDEN_RATIO) * parent.getHeight()));
            }

            configuration.setSettled(true);
            headerConfig.setSettled(true);
            footerConfig.setSettled(true);
            cancelAnimation();
            setTopAndBottomOffset(headerConfig.getVisibleHeight());
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollingListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, configuration.getMaxOffset(), type == TYPE_TOUCH);
            }
        }
        return start;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0) {
            // When scrolling up, compute the top offset which content can reach.
            int topOffset = configuration.getMinOffset();
            if (child.getTop() <= topOffset)
                return;
            int offset = MathUtils.clamp(-dy, topOffset - child.getTop(), 0);
            if (offset != 0) {
                consumeOffset(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        } else if (dy < 0) {
            // When scrolling down, if footer is still visible.
            if (child.getBottom() < coordinatorLayout.getHeight()) {
                int offset = MathUtils.clamp(-dy, 0, coordinatorLayout.getHeight() - child.getBottom());
                consumeOffset(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // If there is unconsumed pixels.
        if (dyUnconsumed < 0) {
            // Scrolling down.
            // If top position of child can not scroll exceed maximum offset.
            if (child.getTop() >= configuration.getMaxOffset())
                return;
            int offset = MathUtils.clamp(-dyUnconsumed, 0, configuration.getMaxOffset() - child.getTop());
            if (offset != 0) {
                if (child.getTop() >= headerConfig.getVisibleHeight()) {
                    // When header's visible part is totally visible, do not consume none touch scroll,
                    // content can scroll to the maximum offset with the touch.
                    if (type != TYPE_TOUCH)
                        return;
                    consumeOffset(coordinatorLayout, child, offset, type, false);
                } else {
                    // Recompute the offset so that the top does not exceed headerVisibleHeight.
                    offset = MathUtils.clamp(-dyUnconsumed, 0, headerConfig.getVisibleHeight() - child.getTop());
                    consumeOffset(coordinatorLayout, child, offset, type, true);
                }
            }
        } else if (dyUnconsumed > 0) {
            // Scrolling up.
            // Can not scroll exceed footer maximum offset.
            int maxFooterTopBottomOffset = coordinatorLayout.getHeight() - footerConfig.getMaxOffset();
            if (child.getBottom() <= maxFooterTopBottomOffset)
                return;
            int offset = MathUtils.clamp(-dyUnconsumed, maxFooterTopBottomOffset - child.getBottom(), 0);
            if (offset != 0) {
                if (coordinatorLayout.getHeight() - child.getBottom() >= footerConfig.getVisibleHeight()) {
                    // If footer's visible part is totally visible, ignore fling too.
                    if (type != TYPE_TOUCH)
                        return;
                    consumeOffset(coordinatorLayout, child, offset, type, false);
                } else {
                    // Recompute it.
                    offset = MathUtils.clamp(-dyUnconsumed, -(footerConfig.getVisibleHeight() - coordinatorLayout.getHeight() + child.getBottom()), 0);
                    consumeOffset(coordinatorLayout, child, offset, type, true);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        for (ScrollingListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, getTopAndBottomOffset(), configuration.getMaxOffset(), type == TYPE_TOUCH);
        }
    }

    /**
     * @param coordinatorLayout
     * @param child
     * @param offsetDelta
     * @param type
     * @param consumeRawOffset  consume raw offset or not, eg. for a smooth fling action we may not just keep it.
     * @return
     */
    private int consumeOffset(CoordinatorLayout coordinatorLayout, View child, int offsetDelta, int type, boolean consumeRawOffset) {
        int currentOffset = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, currentOffset, configuration.getMaxOffset(), type == TYPE_TOUCH);
        }
        float consumed = consumeRawOffset ? offsetDelta : onConsumeOffset(currentOffset, configuration.getMaxOffset(), offsetDelta);
        currentOffset = Math.round(currentOffset + consumed);
        setTopAndBottomOffset(currentOffset);
//        Timber.d("setTopAndBottomOffset: %d", currentOffset);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's onNestedScroll().
        // The content view itself can make some transformation by setTranslationY() that may keep it's drawing rectangle
        // unchanged while it's offset has changed. In this case CoordinatorLayout will not call onDependentViewChanged().
        // So We need to call onDependentViewChanged() manually.
        coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, currentOffset, offsetDelta, configuration.getMaxOffset(), type == TYPE_TOUCH);
        }
        return currentOffset;
    }

    protected float onConsumeOffset(int current, int max, int delta) {
        return delta;
    }

    /**
     * This will reset the header or footer view to it's original position when it's laid out for the first time.
     */
    protected void reset(long animateDuration) {
        if (null == getChild() || getParent() == null) return;

        // Reset footer first, then consider header.
        // Based on a strong contract that headerVisibleHeight is a distance from parent top.
        // We must make sure
        int offset;
        if (-getChild().getBottom() + getParent().getHeight() > 0) {
            // Footer is visible now.
            if (isMinOffsetReached()) {
                offset = configuration.getMinOffset() - getChild().getTop();
            } else {
                offset = getParent().getHeight() - getChild().getBottom();
            }
        } else {
            offset = headerConfig.getVisibleHeight() - getChild().getTop();
        }
        Timber.d("reset: %d %d %d", offset, getChild().getTop(), getTopAndBottomOffset());
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset, animateDuration);
    }

    /**
     * Make the header view entirely visible.
     */
    protected void showHeader(long animateDuration) {
        if (null == getChild() || null == getParent()) return;
        int offset = headerConfig.getHeight() - getChild().getTop();
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset, animateDuration);
    }

    void showFooter(long animationDuration) {
        if (null == getChild() || getParent() == null) return;
        int offset = getParent().getHeight() - footerConfig.getHeight() - getChild().getBottom();
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset, animationDuration);
    }

    private Runnable offsetCallback;

    /**
     * If view has scroll to a invalid position, reset it, otherwise do nothing.
     *
     * @param holdOn
     */
    protected void stopScroll(boolean holdOn) {
        int currentOffset = getTopAndBottomOffset();
        // If content offset is larger than header's visible height or smaller than minimum offset,
        // which means content has scrolled to a insignificant or invalid position.
        if (currentOffset > headerConfig.getVisibleHeight() || currentOffset < configuration.getMinOffset()) {
            if (getChild() == null || getChild().getHandler() == null) return;
            // Remove previous pending callback.
            handler.removeCallbacks(offsetCallback);
            offsetCallback = new Runnable() {
                @Override
                public void run() {
                    reset(RESET_DURATION);
                }
            };
            handler.postDelayed(offsetCallback, holdOn ? HOLD_ON_DURATION : 0L);
        }
    }

    boolean isMinOffsetReached() {
        return getTopAndBottomOffset() <= configuration.getMinOffset();
    }

    public BehaviorConfiguration createIndicatorConfig() {
        return new BehaviorConfiguration.Builder()
                .setDefaultRefreshTriggerRange(configuration.getDefaultRefreshTriggerRange())
                .setRefreshTriggerRange(configuration.getDefaultRefreshTriggerRange())
                .build();
    }

    public void setHeaderConfig(BehaviorConfiguration headerConfig) {
        this.headerConfig = new BehaviorConfiguration.Builder(headerConfig).setSettled(false).build();
        configuration.setMinOffset(Math.min(configuration.getCachedMinOffset(), headerConfig.getVisibleHeight()));
        requestLayout();
    }

    public void setFooterConfig(BehaviorConfiguration footerConfig) {
        this.footerConfig = new BehaviorConfiguration.Builder(footerConfig).setSettled(false).build();
        requestLayout();
    }

    @Override
    public ContentBehaviorController getController() {
        return super.getController();
    }

    public BehaviorConfiguration getHeaderConfig() {
        return headerConfig;
    }

    public BehaviorConfiguration getFooterConfig() {
        return footerConfig;
    }
}
