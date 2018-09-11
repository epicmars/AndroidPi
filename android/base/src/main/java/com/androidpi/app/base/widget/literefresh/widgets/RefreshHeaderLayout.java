package com.androidpi.app.base.widget.literefresh.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;

/**
 * A scrolling header layout that has a attached {@link RefreshHeaderBehavior}.
 * Created by jastrelax on 2018/8/19.
 */
public class RefreshHeaderLayout extends FrameLayout implements CoordinatorLayout.AttachedBehavior{

    protected RefreshHeaderBehavior behavior;

    public RefreshHeaderLayout(Context context) {
        this(context, null);
    }

    public RefreshHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        behavior = new RefreshHeaderBehavior(context, attrs);
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return behavior;
    }
}
