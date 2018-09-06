package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;


/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View>
        extends VerticalIndicatorBehavior<V, FooterBehaviorController> implements Refresher {

    {
        controller = new FooterBehaviorController(this);
        addScrollListener(controller);
        runWithView(new Runnable() {
            @Override
            public void run() {
                controller.setProxy(getContentBehavior().getController());
            }
        });
    }

    public RefreshFooterBehavior(Context context) {
        this(context, null);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorBehavior,
                0, 0);
        if (a.hasValue(R.styleable.IndicatorBehavior_lr_mode)) {
            int mode = a.getInt(R.styleable.IndicatorBehavior_lr_mode, HeaderBehaviorController.MODE_FOLLOW);
            controller.setMode(mode);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        // The height of content may have changed, so does the footer's initial visible height.
        final int lastInitialVisibleHeight = configuration.getInitialVisibleHeight();
        final int currentInitialVisibleHeight = getInitialVisibleHeight(child);
        if (lastInitialVisibleHeight != currentInitialVisibleHeight) {
            configuration.setSettled(false);
        }
        if (!configuration.isSettled()) {
            CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
            // Compute max offset, it will not exceed parent height.
            if (configuration.isUseDefaultMaxOffset()) {
                // We want footer can be just fully visible by default.
                configuration.setMaxOffset(child.getHeight());
            } else {
                configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(),
                        configuration.getMaxOffsetRatioOfParent()
                                > configuration.getMaxOffsetRatio()
                                ? configuration.getMaxOffsetRatio() * parent.getHeight()
                                : configuration.getMaxOffsetRatio() * child.getHeight()));
            }
            configuration.setHeight(child.getHeight());
            configuration.setInitialVisibleHeight(currentInitialVisibleHeight);
            if (configuration.getInitialVisibleHeight() <= 0) {
                // If initial visible height is non-positive, add the top margin to refresh trigger range.
                configuration.setRefreshTriggerRange(configuration.getRefreshTriggerRange() + lp.topMargin);
            }
            // Maximum offset should not be less than initial visible height.
            configuration.setMaxOffset(Math.max(configuration.getMaxOffset(),
                    configuration.getInitialVisibleHeight() + configuration.getRefreshTriggerRange()));
            configuration.setSettled(true);
            getContentBehavior().setFooterConfig(configuration);
        }
        return handled;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    @Override
    public void refresh() {
        controller.load();
    }

    @Override
    public void refreshComplete() {
        controller.loadComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        controller.loadError(throwable);
    }

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
    }

    @Override
    public FooterBehaviorController getController() {
        return super.getController();
    }


    @Override
    protected int getInitialOffset() {
        return -configuration.getVisibleHeight() + getParent().getHeight();
    }

    @Override
    protected int getMinOffset() {
        return -configuration.getTopMargin();
    }

    @Override
    protected int getMaxOffset() {
        ScrollingContentBehavior contentBehavior = getContentBehavior();
        return contentBehavior == null
                ? 0
                : contentBehavior.getFooterConfig().getMaxOffset() - configuration.getTopMargin();
    }

    private int getInitialVisibleHeight(View child) {
        int initialVisibleHeight;
        if (configuration.getHeight() <= 0 || configuration.getVisibleHeight() <= 0) {
            initialVisibleHeight = configuration.getVisibleHeight();
        } else if (configuration.getVisibleHeight() >= child.getHeight()) {
            initialVisibleHeight = configuration.getVisibleHeight()
                    + configuration.getTopMargin() + configuration.getBottomMargin();
        } else {
            initialVisibleHeight = configuration.getVisibleHeight() + configuration.getTopMargin();
        }
        // If header configuration is not settled when footer is in layout, so we may get a
        // header's initial visible height is zero and we get a footer's initial visible height that
        // fill the parent, after that when we compute a right initial visible height that is smaller,
        // it will not be set.
        ScrollingContentBehavior contentBehavior = getContentBehavior();
        // If content is too short, there may be extra space left.
        if (contentBehavior == null
                || getParent().getHeight() == 0
                || contentBehavior.getChild().getHeight() == 0) {
            return initialVisibleHeight;
        } else {
            return Math.max(initialVisibleHeight, getParent().getHeight()
                    - getParent().getPaddingTop()
                    - getParent().getPaddingBottom()
                    - contentBehavior.getChild().getHeight()
                    - contentBehavior.getConfiguration().getTopMargin()
                    - contentBehavior.getConfiguration().getBottomMargin()
                    - contentBehavior.getHeaderConfig().getInitialVisibleHeight());
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        // We want footer layout after content and header.
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ScrollingContentBehavior || behavior instanceof RefreshHeaderBehavior;
        }
        return false;
    }
}
