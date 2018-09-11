package com.androidpi.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.androidpi.app.R;
import com.androidpi.app.activity.TemplateActivity;

/**
 * Created by jastrelax on 2018/9/6.
 */
public class SampleToolbar extends AppBarLayout{

    AppBarLayout appBar;
    Toolbar toolbar;

    public SampleToolbar(Context context) {
        this(context, null);
    }

    public SampleToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.sample_toolbar, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleToolbar, 0, 0);
        int bgColor = a.getColor(R.styleable.SampleToolbar_toolbar_background, getResources().getColor(R.color.transparent));
        String title = a.getString(R.styleable.SampleToolbar_toolbar_title);
        a.recycle();

        appBar = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        setBackgroundColor(bgColor);
        toolbar.setTitle(title);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(context);
                if (activity instanceof TemplateActivity) {
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Navigation button clicked.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Activity getActivity(Context context) {
        if (context == null)
            return null;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context);
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
