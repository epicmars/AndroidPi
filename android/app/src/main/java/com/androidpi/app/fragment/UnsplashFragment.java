package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.buiness.vo.UnsplashPhotoPage;
import com.androidpi.app.databinding.FragmentUnsplashBinding;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_unsplash)
public class UnsplashFragment extends BaseFragment<FragmentUnsplashBinding> {

    private UnsplashViewModel unsplashViewModel;

    public static UnsplashFragment newInstance() {
        Bundle args = new Bundle();
        UnsplashFragment fragment = new UnsplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    int triggerOffset;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        UnsplashPhotoListFragment fragment = ((UnsplashPhotoListFragment) getChildFragmentManager().findFragmentById(R.id.fragment));
        RefreshHeaderBehavior headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.scaleableHeader);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (null == listResource) return;
                if (listResource.data == null)
                    return;
                if (listResource.isSuccess()) {
                    if (listResource.data.isFirstPage()) {
                        List<ResUnsplashPhoto> photos = listResource.data.getPhotos();
                        if (photos == null || photos.isEmpty()) {
                            fragment.refreshError(new Exception("Empty data."));
                        } else {
                            if (photos.size() > 3) {
                                binding.imagePagerHeader.setImages(photos.subList(0, 3));
                                fragment.setPayloads(photos.subList(3, photos.size()));
                            } else {
                                binding.imagePagerHeader.setImages(photos.subList(0, 1));
                                if (photos.size() > 1) {
                                    fragment.setPayloads(photos.subList(1, photos.size()));
                                }
                            }
                        }
                    } else {
                        fragment.addPayloads(listResource.data.getPhotos());
                    }
                    headerBehavior.refreshComplete();
                } else if (listResource.isError()) {
                    if (listResource.data.isFirstPage()) {
                        fragment.refreshError(listResource.throwable);
                    }
                    headerBehavior.refreshError(listResource.throwable);
                }
            }
        });

        headerBehavior.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {
                triggerOffset = trigger - initial;
            }

            @Override
            public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                float offset = current - initial;
                float triggerRange = trigger - initial;
                float progress = offset / (trigger - initial);
                binding.loadingView.setProgress(progress);
                binding.loadingView.setTranslationY(MathUtils.clamp(offset, 0, triggerRange));
            }

            @Override
            public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

            }
        });

        headerBehavior.addOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefreshStart() {
                binding.loadingView.startProgress();
            }

            @Override
            public void onReleaseToRefresh() {
                binding.loadingView.setTranslationY(triggerOffset);
                binding.loadingView.readyToLoad();
            }

            @Override
            public void onRefresh() {
                binding.loadingView.startLoading();
                firstPage();
            }

            @Override
            public void onRefreshEnd(Throwable throwable) {
                binding.loadingView.finishLoading();
            }
        });


        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            headerBehavior.refresh();
        }
    }

    private void firstPage() {
        unsplashViewModel.firstPage();
    }
}
