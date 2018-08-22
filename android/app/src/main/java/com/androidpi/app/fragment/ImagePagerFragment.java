package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.databinding.FragmentImagePagerBinding;
import com.androidpi.app.viewholder.UnsplashImageViewHolder;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_image_pager)
public class ImagePagerFragment extends BaseFragment<FragmentImagePagerBinding> {

    RecyclerAdapter adapter;

    UnsplashViewModel unsplashViewModel;

    public static ImagePagerFragment newInstance() {
        Bundle args = new Bundle();
        ImagePagerFragment fragment = new ImagePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashImageViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<List<ResUnsplashPhoto>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ResUnsplashPhoto>> listResource) {
                if (null == listResource)
                    return;
                if (listResource.isSuccess()) {
                    if (listResource.data.size() > 1) {
                        adapter.addPayloads(listResource.data.subList(0, 2));
                    }
                }
            }
        });
    }
}
