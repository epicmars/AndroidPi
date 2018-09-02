package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.base.widget.literefresh.BehaviorConfiguration;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshContentBehavior;
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.buiness.vo.UnsplashPhotoPage;
import com.androidpi.app.databinding.FragmentCollapsibleHeaderBinding;

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
        RefreshHeaderBehavior headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.imagePagerHeader);
        RefreshContentBehavior contentBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewContent);

        binding.circleProgress.setColor(getResources().getColor(R.color.colorPrimary));
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

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Toolbar navigation button clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        if (headerBehavior != null) {
            BehaviorConfiguration config = headerBehavior.getConfiguration();
            contentBehavior.addOnScrollListener(new OnScrollListener() {

                ColorDrawable drawable = new ColorDrawable(Color.BLACK);

                @Override
                public void onStartScroll(View view, int max, boolean isTouch) {

                }

                @Override
                public void onScroll(View view, int current, int delta, int max, boolean isTouch) {
                    if (current <= config.getHeight()) {
                        float y = config.getHeight() - current;
                        binding.imagePagerHeader.setTranslationY(y / 2);
                        float alpha = 1 - (float) current / config.getHeight();
                        drawable.setAlpha((int) (alpha * 196));
                        binding.imagePagerHeader.setForeground(drawable);
                    }

                    binding.circleProgress.setProgress(Math.max(0f, (float) (current - config.getHeight())) / config.getRefreshTriggerRange());

                    if (current >= contentBehavior.getConfiguration().getMinOffset()) {
                        float range = config.getHeight() - contentBehavior.getConfiguration().getMinOffset();
                        float distance = current - contentBehavior.getConfiguration().getMinOffset();
                        float alpha = 1 - distance / range;
                        binding.appBar.setAlpha(alpha);
                        binding.appBar.setTranslationY(alpha * contentBehavior.getConfiguration().getMinOffset());
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
                    unsplashViewModel.firstPage();
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
                    binding.circleProgress.stopLoading();
                    binding.circleProgress.setVisibility(View.GONE);
                }
            });
        }

    }
}
