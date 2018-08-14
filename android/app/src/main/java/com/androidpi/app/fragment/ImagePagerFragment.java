package com.androidpi.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.BaseFragment;
import com.androidpi.app.base.BindLayout;
import com.androidpi.app.databinding.FragmentImagePagerBinding;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_image_pager, injectable = false)
public class ImagePagerFragment extends BaseFragment<FragmentImagePagerBinding> {

    public static ImagePagerFragment newInstance() {

        Bundle args = new Bundle();

        ImagePagerFragment fragment = new ImagePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
    }
}
