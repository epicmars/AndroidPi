package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import io.reactivex.annotations.Nullable;

/**
 * Created by jastrelax on 2018/8/25.
 */
public class LiteRefreshHelper {

    @Nullable
    public static <T extends AnimationOffsetBehavior> T getAttachedBehavior(View view) {
        try {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            return (T) params.getBehavior();
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public static ContentBehavior getContentBehavior(View view) {
        return getAttachedBehavior(view);
    }

    @Nullable
    public static HeaderBehavior getHeaderBehavior(View view) {
        return getAttachedBehavior(view);
    }

    @Nullable
    public static FooterBehavior getFooterBehavior(View view) {
        return getAttachedBehavior(view);
    }
}
