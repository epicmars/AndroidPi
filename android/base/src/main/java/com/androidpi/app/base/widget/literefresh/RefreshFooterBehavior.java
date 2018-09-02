package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View> extends VerticalIndicatorBehavior<V, FooterBehaviorController> implements Loader{

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
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (!configuration.isSettled()) {
            // Compute max offset, it will not exceed parent height.
            if (configuration.isUseDefaultMaxOffset()) {
                // We want footer can be just fully visible by default.
                configuration.setMaxOffset(child.getHeight());
            } else {
                configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(), configuration.getMaxOffsetRatioOfParent() > configuration.getMaxOffsetRatio() ? configuration.getMaxOffsetRatio() * parent.getHeight() : configuration.getMaxOffsetRatio() * child.getHeight()));
            }
            configuration.setHeight(child.getHeight());
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

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public void refreshError(Throwable throwable) {

    }

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
    }

    @Override
    public void load() {
        controller.load();
    }

    @Override
    public void loadComplete() {
        controller.loadComplete();
    }

    @Override
    public void loadError(Exception exception) {
        controller.loadError(exception);
    }

    @Override
    public FooterBehaviorController getController() {
        return super.getController();
    }
}
