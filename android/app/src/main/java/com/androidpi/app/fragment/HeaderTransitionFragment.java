package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.databinding.FragmentHeaderTransitionBinding;
import com.androidpi.data.remote.dto.ResRandomPhotos;

import java.util.List;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/8/19.
 */
@BindLayout(R.layout.fragment_header_transition)
public class HeaderTransitionFragment extends BaseFragment<FragmentHeaderTransitionBinding>{

    UnsplashViewModel unsplashViewModel;
    String url;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unsplashViewModel = getViewModel(UnsplashViewModel.class);
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<List<ResRandomPhotos>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ResRandomPhotos>> listResource) {
                if (listResource == null) return;
                if (listResource.isSuccess()) {
                    try {
                        ResRandomPhotos resRandomPhotos = listResource.data.get(0);
                        url = resRandomPhotos.getUrls().getRegular();
                        binding.viewHeader.setUrl(url);
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
            }
        });

        unsplashViewModel.getRandomPhotos(1);
    }
}
