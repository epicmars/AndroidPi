package com.androidpi.app.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;

import com.androidpi.app.R;
import com.androidpi.app.activity.VideoActivity;
import com.androidpi.app.base.widget.literefresh.widgets.RefreshHeaderLayout;

/**
 * Created by jastrelax on 2018/8/20.
 */
public class VideoHeaderView extends RefreshHeaderLayout {

    private boolean launched;

    public VideoHeaderView(Context context) {
        this(context, null);
    }

    public VideoHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_video_header, this);
    }

    @Override
    public void onStartScroll(int max, boolean isTouch) {
        if (isTouch) {
            launched = false;
        }
    }

    @Override
    public void onScroll(int current, int delta, int max, boolean isTouch) {
        if (!isTouch || launched) return;
        if ((current /(float) max) >= 0.85f) {
            String sharedElementName = getResources().getString(R.string.transition_header);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), this, sharedElementName);
            VideoActivity.Companion.start(getContext(), options.toBundle());
            launched = true;
        }
    }

    @Override
    public void onStopScroll(int current, int max) {
    }

}
