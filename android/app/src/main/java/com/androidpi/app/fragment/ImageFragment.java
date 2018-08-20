package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentImageBinding;
import com.androidpi.common.image.glide.GlideApp;

/**
 * Created by jastrelax on 2018/8/19.
 */
@BindLayout(R.layout.fragment_image)
public class ImageFragment extends BaseFragment<FragmentImageBinding>{

    public static final String ARGS_IMAGE_URL = "ImageFragment.ARGS_IMAGE_URL";

    public static ImageFragment newInstance(String imageUrl) {
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_URL, imageUrl);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageFragment newInstance(Bundle args) {
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String imageUrl = getArguments().getString(ARGS_IMAGE_URL);
            GlideApp.with(view)
                    .load(imageUrl)
                    .into(binding.ivImage);
        }
    }
}
