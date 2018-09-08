package com.androidpi.app.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.androidpi.app.fragment.ImageHeaderFragment;
import com.androidpi.app.fragment.TempFragment;
import com.androidpi.app.fragment.TrendingImageHeaderFragment;
import com.androidpi.data.remote.dto.ResTrendingPage;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageHeaderPagerAdapter extends FragmentStatePagerAdapter {

        private List<Object> mPhotos = new ArrayList<>();

        public ImageHeaderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Object photo = mPhotos.get(position);
            if (photo instanceof ResUnsplashPhoto) {
                return ImageHeaderFragment.newInstance(((ResUnsplashPhoto) photo).getUrls().getSmall());
            } else if (photo instanceof ResTrendingPage.ResultsBean) {
                return TrendingImageHeaderFragment.newInstance(((ResTrendingPage.ResultsBean) photo).getPosterPath());
            } else {
                return TempFragment.Companion.newInstance("");
            }
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        public void setPhotos(Collection<?> photos) {
            if (photos == null || photos.isEmpty())
                return;
            mPhotos.clear();
            mPhotos.addAll(photos);
            notifyDataSetChanged();
        }
    }