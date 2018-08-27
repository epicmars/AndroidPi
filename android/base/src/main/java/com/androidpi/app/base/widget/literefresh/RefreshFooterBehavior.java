package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View> extends VerticalIndicatorBehavior<V, FooterBehaviorController> implements Loader{

    private boolean isFirstLayout = true;

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
        super(context);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (isFirstLayout) {
            // Compute max offset, it will not exceed parent height.
            if (useDefaultMaxOffset) {
                // We want footer can be just fully visible by default.
                maxOffset = child.getHeight();
            } else {
                maxOffset = Math.max(maxOffset, maxOffsetRatioOfParent > maxOffsetRatio ? maxOffsetRatio * parent.getHeight() : maxOffsetRatio * child.getHeight());
            }
            getContentBehavior().setFooterVisibleHeight(getVisibleHeight());
            getContentBehavior().setFooterHeight(child.getHeight());
            getContentBehavior().setFooterMaxOffset((int) maxOffset);
            isFirstLayout = false;
        }
        return handled;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnPullingListener(listener);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public void refreshError(Exception exception) {

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

}
