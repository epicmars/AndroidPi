package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    boolean isRefresh = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UnsplashPhotoListFragment fragment = ((UnsplashPhotoListFragment) getChildFragmentManager().findFragmentById(R.id.fragment));
        RefreshHeaderBehavior headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.imagePagerHeader);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (null == listResource) return;
                if (listResource.data == null)
                    return;
                if (listResource.isSuccess()) {
                    if (listResource.data.isFirstPage()) {
                        fragment.setPayloads(listResource.data.getPhotos());
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
            public void onStartScroll(View view, int max, boolean isTouch) {
                if (isTouch) {
                    binding.loadingView.startProgress();
                    binding.loadingView.setProgress(0);
                }
            }

            @Override
            public void onScroll(View view, int current, int delta, int max, boolean isTouch) {
                float offset = current - view.getHeight();
                float progress = offset / headerBehavior.getConfiguration().getRefreshTriggerRange();
                binding.loadingView.setProgress(progress);
                // Because the scrolling event is dispatched before the refresh event.
                // The logic here should not depend on the refresh state.
                if (delta > 0 || !isRefresh) {
                    binding.loadingView.setTranslationY(MathUtils.clamp(offset, 0, headerBehavior.getConfiguration().getRefreshTriggerRange()));
                }
            }

            @Override
            public void onStopScroll(View view, int current, int max, boolean isTouch) {

            }
        });

        headerBehavior.addOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefreshStart() {
                binding.loadingView.animate().cancel();
                binding.loadingView.startProgress();
            }

            @Override
            public void onReleaseToRefresh() {
                binding.loadingView.setTranslationY(headerBehavior.getConfiguration().getRefreshTriggerRange());
                binding.loadingView.readyToLoad();
            }

            @Override
            public void onRefresh() {
                isRefresh = true;
                binding.loadingView.startLoading();
                refreshPhotos();
            }

            @Override
            public void onRefreshEnd(Throwable throwable) {
                isRefresh = false;
                binding.loadingView.finishLoading();
                binding.loadingView.animate().setDuration(300).translationY(0);
            }
        });

        refreshPhotos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsplashViewModel.getRandomPhotosResult().removeObservers(this);
    }

    private void refreshPhotos() {
        unsplashViewModel.firstPage();
    }
}
