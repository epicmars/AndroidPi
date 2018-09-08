package com.androidpi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.R;
import com.androidpi.app.adapter.ImageHeaderPagerAdapter;
import com.androidpi.app.fragment.ImageHeaderFragment;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by jastrelax on 2018/8/13.
 */
public class ImagePagerHeaderView extends FrameLayout {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageHeaderPagerAdapter pagerAdapter;

    public ImagePagerHeaderView(Context context) {
        this(context, null);
    }

    public ImagePagerHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePagerHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_image_pager, this);
        viewPager = findViewById(R.id.image_view_pager);
        circleIndicator = findViewById(R.id.circle_indicator);
    }

    public void setFragmentManager(FragmentManager fm) {
        pagerAdapter = new ImageHeaderPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(viewPager);
    }

    public void setImages(List<?> photos) {
        if (pagerAdapter != null) {
            pagerAdapter.setPhotos(photos);
            circleIndicator.setViewPager(viewPager);
        }
    }
}
