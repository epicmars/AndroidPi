package com.androidpi.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.app.R;
import com.androidpi.app.adapter.ImageHeaderPagerAdapter;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by jastrelax on 2018/8/13.
 */
public class ImagePagerHeaderView extends FrameLayout {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageHeaderPagerAdapter pagerAdapter;
    private boolean autoFlip = false;
    private boolean autoFlipStarted = false;
    private Handler handler;

    public ImagePagerHeaderView(Context context) {
        this(context, null);
    }

    public ImagePagerHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePagerHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImagePagerHeaderView, 0, 0);
        autoFlip = a.getBoolean(R.styleable.ImagePagerHeaderView_auto_flip, false);
        a.recycle();
        inflate(context, R.layout.view_image_pager, this);
        viewPager = findViewById(R.id.image_view_pager);
        circleIndicator = findViewById(R.id.circle_indicator);
    }

    public void setFragmentManager(FragmentManager fm) {
        pagerAdapter = new ImageHeaderPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(viewPager);
        autoFlip();
    }

    public void setImages(List<?> photos) {
        if (pagerAdapter != null) {
            pagerAdapter.setPhotos(photos);
            circleIndicator.setViewPager(viewPager);
        }
    }

    private Runnable autoFlipTask = new Runnable() {
        @Override
        public void run() {
            if (!autoFlip) {
                return;
            }
            int next = viewPager.getCurrentItem() + 1;
            int total = pagerAdapter.getCount();
            if (next >= total) {
                next = 0;
            }
            viewPager.setCurrentItem(next, true);
            handler.postDelayed(this, 5000L);
        }
    };

    public void autoFlip() {
        if (autoFlipStarted)
            return;
        autoFlipStarted = true;
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(autoFlipTask, 3000L);
    }

    public void finishAutoFlip() {
        autoFlip = false;
        autoFlipStarted = false;
        if (handler != null) {
            handler.removeCallbacks(autoFlipTask);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        finishAutoFlip();
        super.onDetachedFromWindow();
    }
}
