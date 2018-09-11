package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Toast;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshContentBehavior;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.buiness.vo.UnsplashPhotoPage;
import com.androidpi.app.databinding.FragmentCollapsibleHeaderBinding;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.fragment_collapsible_header)
public class CollapsibleHeaderFragment extends BaseFragment<FragmentCollapsibleHeaderBinding> {

    UnsplashViewModel unsplashViewModel;

    public static CollapsibleHeaderFragment newInstance() {
        Bundle args = new Bundle();
        CollapsibleHeaderFragment fragment = new CollapsibleHeaderFragment();
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
        RefreshHeaderBehavior headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewHeader);
        RefreshContentBehavior contentBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewContent);

        binding.circleProgress.setColor(getResources().getColor(R.color.colorAccent));
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        UnsplashPhotoGridFragment fragment = ((UnsplashPhotoGridFragment) getChildFragmentManager().findFragmentById(R.id.fragment));
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (null == listResource)
                    return;
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

        if (headerBehavior != null) {
            contentBehavior.addOnScrollListener(new OnScrollListener() {

                ColorDrawable drawable = new ColorDrawable(Color.BLACK);

                @Override
                public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

                }

                @Override
                public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                    // set header's translation
                    if (current <= initial) {
                        float y = initial - current;
                        binding.viewHeader.setTranslationY(y / 2);
                        float alpha = 1 - (float) current / initial;
                        drawable.setAlpha((int) (alpha * 196));
                        binding.viewHeader.setForeground(drawable);
                    }

                    // set progress
                    binding.circleProgress.setProgress(Math.max(0f, (float) current / trigger));

                    // set appbar's translation
                    if (current >= min) {
                        float rangeMax = initial - min;
                        float distance = current - min;
                        float alpha = 1 - distance / rangeMax;
                        binding.appBar.setAlpha(alpha);
                        binding.appBar.setTranslationY(alpha * min);
                    }
                }

                @Override
                public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

                }
            });

            headerBehavior.addOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefreshStart() {
                    binding.circleProgress.resetStyle();
                }

                @Override
                public void onReleaseToRefresh() {
                    binding.circleProgress.fillCircle();
                }

                @Override
                public void onRefresh() {
                    binding.circleProgress.startLoading();
                    unsplashViewModel.firstPage();
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
                    binding.circleProgress.stopLoading();
                }
            });
        }

        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            headerBehavior.refresh();
        }
    }
}
