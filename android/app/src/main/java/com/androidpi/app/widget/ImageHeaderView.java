package com.androidpi.app.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.androidpi.app.R;
import com.androidpi.app.activity.TemplateActivity;
import com.androidpi.app.base.widget.literefresh.widgets.ScrollingHeaderLayout;
import com.androidpi.app.fragment.FragmentFactory;
import com.androidpi.app.fragment.ImageFragment;
import com.androidpi.common.image.glide.GlideApp;

/**
 * Created by jastrelax on 2018/8/19.
 */
public class ImageHeaderView extends ScrollingHeaderLayout {

    private Bundle data;
    private ImageView ivImage;
    private boolean launched = false;

    public ImageHeaderView(Context context) {
        this(context, null);
    }

    public ImageHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_image_header, this);
        ivImage = findViewById(R.id.iv_image);
    }

    @Override
    public void onStartScroll(int max, boolean isTouch) {
        if (isTouch) {
            launched = false;
        }
    }

    @Override
    public void onScroll(int current, int delta, int max, boolean isTouch) {
        if (!isTouch || data == null || launched) return;
        if ((current /(float) max) >= 1f) {
            String sharedElementName = getResources().getString(R.string.transition_header);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), this, sharedElementName);
            TemplateActivity.Companion.startWith(options, getContext(), 0, ImageFragment.class.getName(), new FragmentFactory<Fragment>() {
                @Override
                public Fragment create() {
                    return ImageFragment.newInstance(data);
                }
            });
            launched = true;
        }
    }

    @Override
    public void onStopScroll(int current, int max) {
    }

    public void setUrl(String url) {
        GlideApp.with(this)
                .load(url)
                .into(ivImage);
        this.data = new Bundle();
        data.putString(ImageFragment.ARGS_IMAGE_URL, url);
    }
}
