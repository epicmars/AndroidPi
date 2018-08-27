package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.databinding.FragmentUnsplashBinding;
import com.androidpi.app.viewholder.UnsplashPhotoGridViewHolder;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_unsplash)
public class UnsplashFragment extends BaseFragment<FragmentUnsplashBinding> {

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
        adapter.register(UnsplashPhotoGridViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<List<ResUnsplashPhoto>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ResUnsplashPhoto>> listResource) {
                if (null == listResource) return;
                if (listResource.isSuccess()) {
                    adapter.addPayloads(listResource.data);
                }
            }
        });
        unsplashViewModel.getRandomPhotos(20);
    }
}
