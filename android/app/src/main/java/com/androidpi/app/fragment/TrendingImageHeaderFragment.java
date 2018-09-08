package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentTrendingImageHeaderBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.TheMovieDbApi;

/**
 * Created by jastrelax on 2018/9/8.
 */

@BindLayout(R.layout.fragment_trending_image_header)
public class TrendingImageHeaderFragment extends BaseFragment<FragmentTrendingImageHeaderBinding> {

    private static final String ARGS_IMAGE_URL = "TrendingImageHeaderFragment.ARGS_IMAGE_URL";

    public static TrendingImageHeaderFragment newInstance(String imageUrl) {
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_URL, imageUrl);
        TrendingImageHeaderFragment fragment = new TrendingImageHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) return;
        GlideApp.with(view)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        getArguments().getString(ARGS_IMAGE_URL))
                .into(binding.ivImage);
    }
}
