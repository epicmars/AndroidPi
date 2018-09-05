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

public class RefreshFooterBehavior<V extends View> extends VerticalIndicatorBehavior<V, FooterBehaviorController> implements Refresher {

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorBehavior, 0, 0);
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
                configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(), configuration.getMaxOffsetRatioOfParent() > configuration.getMaxOffsetRatio() ? configuration.getMaxOffsetRatio() * parent.getHeight() : configuration.getMaxOffsetRatio() * child.getHeight()));
            }
            configuration.setHeight(child.getHeight());
            configuration.setInitialVisibleHeight(currentInitialVisibleHeight);
            if (configuration.getInitialVisibleHeight() <= 0) {
                // IF initial visible height is non-positive, add the top margin to refresh trigger range.
                configuration.setRefreshTriggerRange(configuration.getRefreshTriggerRange() + lp.topMargin);
            }
            // Maximum offset should not be less than initial visible height.
            configuration.setMaxOffset(Math.max(configuration.getMaxOffset(), configuration.getInitialVisibleHeight() + configuration.getRefreshTriggerRange()));
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


    public int getInitialVisibleHeight(View child) {
        int initialVisibleHeight;
        if (configuration.getHeight() <= 0 || configuration.getVisibleHeight() <= 0) {
            initialVisibleHeight = configuration.getVisibleHeight();
        } else if (configuration.getVisibleHeight() >= child.getHeight()) {
            initialVisibleHeight = configuration.getVisibleHeight() + configuration.getTopMargin() + configuration.getBottomMargin();
        } else {
            initialVisibleHeight = configuration.getVisibleHeight() + configuration.getTopMargin();
        }
        // If content is too short, there may be extra space left.
        initialVisibleHeight = Math.max(initialVisibleHeight, getParent().getHeight() - getContentBehavior().getChild().getHeight() - getContentBehavior().getConfiguration().getTopMargin() - getContentBehavior().getConfiguration().getBottomMargin()
                - getContentBehavior().getHeaderConfig().getInitialVisibleHeight());
        return initialVisibleHeight;
    }
}
