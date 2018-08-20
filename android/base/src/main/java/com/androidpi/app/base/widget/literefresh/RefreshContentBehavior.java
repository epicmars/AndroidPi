package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**

 *
 * Created by jastrelax on 2017/11/16.
 */

public class RefreshContentBehavior<V extends View> extends ContentBehavior<V> {

    public RefreshContentBehavior(Context context) {
        super(context);
    }

    public RefreshContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
