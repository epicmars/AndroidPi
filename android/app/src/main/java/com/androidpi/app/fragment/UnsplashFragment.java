package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.BaseFragment;
import com.androidpi.app.base.BindLayout;
import com.androidpi.app.base.RecyclerAdapter;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.buiness.viewmodel.vo.Resource;
import com.androidpi.app.databinding.FragmentUnsplashBinding;
import com.androidpi.app.viewholder.UnsplashImageViewHolder;
import com.androidpi.data.remote.dto.ResRandomPhotos;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_unsplash, injectable = false)
public class UnsplashFragment extends BaseFragment<FragmentUnsplashBinding>{

    private UnsplashViewModel unsplashViewModel;
    private RecyclerAdapter adapter;

    public static UnsplashFragment newInstance() {
        Bundle args = new Bundle();
        UnsplashFragment fragment = new UnsplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UnsplashFragment() {
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashImageViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unsplashViewModel = ViewModelProviders.of(this).get(UnsplashViewModel.class);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<List<ResRandomPhotos>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ResRandomPhotos>> listResource) {
                if (null == listResource) return;
                if (listResource.isSuccess()) {
                    adapter.addPayloads(listResource.data);
                }
            }
        });
        unsplashViewModel.getRandomPhotos(20);
    }
}
