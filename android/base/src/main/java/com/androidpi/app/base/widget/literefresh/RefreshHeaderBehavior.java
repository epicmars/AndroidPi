package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.app.pi.base.R;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class RefreshHeaderBehavior<V extends View>
        extends VerticalIndicatorBehavior<V, HeaderBehaviorController> implements Refresher{

    {
        controller = new HeaderBehaviorController(this);
        addScrollListener(controller);
        runWithView(new Runnable() {
            @Override
            public void run() {
                controller.setProxy(getContentBehavior().getController());
            }
        });
    }

    public RefreshHeaderBehavior(Context context) {
        this(context, null);
    }

    public RefreshHeaderBehavior(Context context, AttributeSet attrs) {
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
        if (!configuration.isSettled()) {
            CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
            // Compute max offset, it will not exceed parent height.
            if (configuration.isUseDefaultMaxOffset()) {
                // We want child can be fully visible by default.
                configuration.setMaxOffset((int) Math.max(GOLDEN_RATIO * parent.getHeight(), child.getHeight()));
            } else {
                configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(),
                        configuration.getMaxOffsetRatioOfParent() > configuration.getMaxOffsetRatio()
                                ? configuration.getMaxOffsetRatio() * parent.getHeight()
                                : configuration.getMaxOffsetRatio() * child.getHeight()));
            }
            configuration.setHeight(child.getHeight());
            configuration.setInitialVisibleHeight(getInitialVisibleHeight());
            if (configuration.getInitialVisibleHeight() <= 0) {
                // IF initial visible height is non-positive, add the bottom margin to refresh trigger range.
                configuration.setRefreshTriggerRange(configuration.getRefreshTriggerRange() + lp.bottomMargin);
            }
            configuration.setMaxOffset(Math.max(configuration.getMaxOffset(),
                    configuration.getInitialVisibleHeight() + configuration.getRefreshTriggerRange()));
            configuration.setSettled(true);
            getContentBehavior().setHeaderConfig(configuration);
        }
        return handled;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
    }

    @Override
    public HeaderBehaviorController getController() {
        return super.getController();
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    @Override
    public void refreshComplete() {
        controller.refreshComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        controller.refreshError(throwable);
    }


    public int getInitialVisibleHeight() {
        int initialVisibleHeight;
        if (configuration.getHeight() <= 0 || configuration.getVisibleHeight() <= 0) {
            initialVisibleHeight = configuration.getVisibleHeight();
        } else if (configuration.getVisibleHeight() >= configuration.getHeight()) {
            initialVisibleHeight = configuration.getVisibleHeight() + configuration.getTopMargin()
                    + configuration.getBottomMargin();
        } else {
            initialVisibleHeight = configuration.getVisibleHeight() + configuration.getBottomMargin();
        }
        return initialVisibleHeight;
    }
}
