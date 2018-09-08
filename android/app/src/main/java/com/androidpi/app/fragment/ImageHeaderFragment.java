package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidpi.app.R;
import com.androidpi.common.image.glide.GlideApp;

public class ImageHeaderFragment extends Fragment {

    private static final String ARGS_IMAGE_URL = "ARGS_IMAGE_URL";

    public static ImageHeaderFragment newInstance(String imageUrl) {
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_URL, imageUrl);
        ImageHeaderFragment fragment = new ImageHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ImageHeaderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) return;
        ImageView imageView = view.findViewById(R.id.iv_image);
        GlideApp.with(imageView)
                .load(getArguments().getString(ARGS_IMAGE_URL))
                .into(imageView);
    }
}