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

public class RefreshHeaderBehavior<V extends View> extends VerticalIndicatorBehavior<V, HeaderBehaviorController> implements Refresher{

    private boolean isFirstLayout = true;

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderBehavior, 0, 0);
        if (a.hasValue(R.styleable.HeaderBehavior_lr_headerMode)) {
            int mode = a.getInt(R.styleable.HeaderBehavior_lr_headerMode, HeaderBehaviorController.MODE_FOLLOW);
            controller.setMode(mode);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (isFirstLayout) {
            isFirstLayout = false;
            // Compute max offset, it will not exceed parent height.
            if (useDefaultMaxOffset) {
                // We want child can be fully visible by default.
                maxOffset = Math.max(GOLDEN_RATIO * parent.getHeight(), child.getHeight());
            } else {
                maxOffset = Math.max(maxOffset, maxOffsetRatioOfParent > maxOffsetRatio? maxOffsetRatio * parent.getHeight() : maxOffsetRatio * child.getHeight());
            }
            getContentBehavior().setHeaderVisibleHeight(getVisibleHeight());
            getContentBehavior().setHeaderHeight(child.getHeight());
            getContentBehavior().setMaxOffset(maxOffset);
        }
        return handled;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnPullingListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
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
    public void refreshError(Exception exception) {
        controller.refreshError(exception);
    }
}
