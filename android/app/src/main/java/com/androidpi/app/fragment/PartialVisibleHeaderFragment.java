package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.activity.TemplateActivity;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnLoadListener;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshFooterBehavior;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.buiness.vo.UnsplashPhotoPage;
import com.androidpi.app.databinding.FragmentPartialVisibleHeaderBinding;

/**
 * Created by jastrelax on 2018/8/26.
 */
@BindLayout(R.layout.fragment_partial_visible_header)
public class PartialVisibleHeaderFragment extends BaseFragment<FragmentPartialVisibleHeaderBinding>{

    UnsplashViewModel unsplashViewModel;
    UnsplashPhotoListFragment photoListFragment;
    private boolean isLaunched = false;
    RefreshHeaderBehavior headerBehavior;
    RefreshFooterBehavior footerBehavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewHeader);
        footerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewFooter);
        photoListFragment = ((UnsplashPhotoListFragment) getChildFragmentManager().findFragmentById(R.id.fragment_list));
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (listResource == null)
                    return;
                if (listResource.data == null)
                    return;
                if (listResource.isSuccess()) {
                    headerBehavior.refreshComplete();
                    if (listResource.data.isFirstPage()) {
                        photoListFragment.setPayloads(listResource.data.getPhotos());
                    } else {
                        photoListFragment.addPayloads(listResource.data.getPhotos());
                    }
                } else if (listResource.isError()) {
                    headerBehavior.refreshError(listResource.throwable);
                    if (listResource.data.isFirstPage()) {
                        photoListFragment.refreshError(listResource.throwable);
                    }
                }
            }
        });

        if (headerBehavior != null) {
            headerBehavior.addOnScrollListener(new OnScrollListener() {

                @Override
                public void onStartScroll(View view, int max, boolean isTouch) {
//                    binding.circleProgress.setVisibility(View.VISIBLE);
//                    circularProgressDrawable.start();
                }

                @Override
                public void onScroll(View view, int current, int delta, int max, boolean isTouch) {
                    if (current >= headerBehavior.getConfiguration().getHeight() * 0.8f) {
                        if (!isLaunched) {
                            isLaunched = true;
                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String sharedElementName = getResources().getString(R.string.transition_header);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, sharedElementName);
                                    TemplateActivity.Companion.startWith(options, getContext(), 0, ImageFragment.class.getName(), new FragmentFactory<Fragment>() {
                                        @Override
                                        public Fragment create() {
                                            return ImageFragment.newInstance((String) null);
                                        }
                                    });
                                }
                            }, 300L);
                        }
                        return;
                    }
                    if (current >= headerBehavior.getConfiguration().getVisibleHeight()) {
                        float distance = current - headerBehavior.getConfiguration().getVisibleHeight();
                        binding.circleProgress.setProgress(distance/headerBehavior.getConfiguration().getRefreshTriggerRange());
                    } else {
                        binding.circleProgress.setProgress(0f);
                    }
                }

                @Override
                public void onStopScroll(View view, int current, int max, boolean isTouch) {
                }
            });

            headerBehavior.addOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefreshStart() {
                    binding.circleProgress.setVisibility(View.VISIBLE);
                    binding.circleProgress.resetStyle();
                }

                @Override
                public void onReleaseToRefresh() {
                    binding.circleProgress.showCircle();
                }

                @Override
                public void onRefresh() {
                    binding.circleProgress.resetStyle();
                    binding.circleProgress.startLoading();
                    if (!isLaunched) {
                        unsplashViewModel.firstPage();
                    } else {
                        headerBehavior.refreshComplete();
                    }
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
                    binding.circleProgress.stopLoading();
                    binding.circleProgress.setVisibility(View.GONE);
                }
            });
        }

        if (footerBehavior != null) {
            footerBehavior.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onStartScroll(View view, int max, boolean isTouch) {

                }

                @Override
                public void onScroll(View view, int current, int delta, int max, boolean isTouch) {
                    float distance = current;
                    binding.footerCircleProgress.setProgress(distance/footerBehavior.getConfiguration().getRefreshTriggerRange());
                }

                @Override
                public void onStopScroll(View view, int current, int max, boolean isTouch) {

                }
            });

//            footerBehavior.addOnLoadListener(new OnLoadListener() {
//                @Override
//                public void onLoadStart() {
//                    binding.footerCircleProgress.setVisibility(View.VISIBLE);
//                    binding.footerCircleProgress.resetStyle();
//                }
//
//                @Override
//                public void onReleaseToLoad() {
//                    binding.footerCircleProgress.showCircle();
//                }
//
//                @Override
//                public void onLoad() {
//                    binding.footerCircleProgress.resetStyle();
//                    binding.footerCircleProgress.startLoading();
//                    binding.footerCircleProgress.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            footerBehavior.refreshComplete();
//                        }
//                    }, 2000L);
//                }
//
//                @Override
//                public void onLoadEnd(@Nullable Throwable throwable) {
//                    binding.footerCircleProgress.stopLoading();
//                    binding.footerCircleProgress.setVisibility(View.GONE);
//                }
//            });
        }

        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            firstPage();
        }
    }

    private void firstPage() {
        unsplashViewModel.firstPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLaunched = false;
    }
}
