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
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshContentBehavior;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.databinding.FragmentImagePagerBinding;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_image_pager)
public class ImagePagerFragment extends BaseFragment<FragmentImagePagerBinding> {

    UnsplashViewModel unsplashViewModel;
    UnsplashPhotoListFragment listFragment;

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        listFragment = ((UnsplashPhotoListFragment) getChildFragmentManager().findFragmentById(R.id.fragment_list));
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<List<ResUnsplashPhoto>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ResUnsplashPhoto>> listResource) {
                if (null == listResource)
                    return;
                if (listResource.isSuccess()) {
                    int max = Math.min(20, listResource.data.size());
                    listFragment.addPayloads(listResource.data);
                }
            }
        });

        RefreshContentBehavior behavior = LiteRefreshHelper.getAttachedBehavior(binding.viewContent);
        if (behavior != null) {
            behavior.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onStartScroll(int max, boolean isTouch) {

                }

                @Override
                public void onScroll(int current, int delta, int max, boolean isTouch) {
                    if (current <= binding.imagePagerHeader.getHeight()) {
                        int y = binding.imagePagerHeader.getHeight() - current;
                        binding.imagePagerHeader.setTranslationY(y / 2);
                    }
                }

                @Override
                public void onStopScroll(int current, int max) {

                }
            });
        }

    }
}
