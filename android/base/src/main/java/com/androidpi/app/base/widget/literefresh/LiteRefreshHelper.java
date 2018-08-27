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
            if (params == null) {
                throw new NullPointerException("You must make sure the view's layout params has been generated." +
                        "If used in custom view, the onAttachedToWindow() method of View is a good hook point where you can make it right.");
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
