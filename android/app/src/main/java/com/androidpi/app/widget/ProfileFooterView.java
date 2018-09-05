package com.androidpi.app.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.androidpi.app.R;

/**
 * Created by jastrelax on 2018/9/5.
 */
public class ProfileFooterView extends ConstraintLayout{

    public ProfileFooterView(Context context) {
        this(context, null);
    }

    public ProfileFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_profile_footer, this);
    }
}
