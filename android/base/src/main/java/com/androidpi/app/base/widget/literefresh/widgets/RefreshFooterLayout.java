package com.androidpi.app.base.widget.literefresh.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.base.widget.literefresh.RefreshFooterBehavior;

/**
 * Created by jastrelax on 2018/9/11.
 */
public class RefreshFooterLayout extends FrameLayout implements CoordinatorLayout.AttachedBehavior{

    protected RefreshFooterBehavior behavior;

    public RefreshFooterLayout(Context context) {
        this(context, null);
    }

    public RefreshFooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshFooterLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        behavior = new RefreshFooterBehavior(context, attrs);
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return behavior;
    }
}
