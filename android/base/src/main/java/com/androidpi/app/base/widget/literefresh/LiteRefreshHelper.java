package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import io.reactivex.annotations.Nullable;

/**
 * Created by jastrelax on 2018/8/25.
 */
public class LiteRefreshHelper {

    public static <T extends AnimationOffsetBehavior> T getAttachedBehavior(View view) {
        try {
            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            if (params == null) {
                throw new NullPointerException(String.format(
                        "Layout params of %s has not been generated yet." +
                                "If used in custom view, the onAttachedToWindow() callback " +
                                "method of android.view.View is a good hook point where you can " +
                                "make it right.",
                        view.getClass().getName()));
            } else {
                return (T) params.getBehavior();
            }
        } catch (ClassCastException e) {
            return null;
        }
    }
//
//    @Nullable
//    public static ContentBehavior getContentBehavior(View view) {
//        return getAttachedBehavior(view);
//    }
//
//    @Nullable
//    public static HeaderBehavior getHeaderBehavior(View view) {
//        return getAttachedBehavior(view);
//    }
//
//    @Nullable
//    public static FooterBehavior getFooterBehavior(View view) {
//        return getAttachedBehavior(view);
//    }
}
