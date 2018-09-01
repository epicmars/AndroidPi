package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.base.widget.literefresh.LiteRefreshHelper;
import com.androidpi.app.base.widget.literefresh.OnRefreshListener;
import com.androidpi.app.base.widget.literefresh.OnScrollListener;
import com.androidpi.app.base.widget.literefresh.RefreshContentBehavior;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.buiness.vo.UnsplashPhotoPage;
import com.androidpi.app.databinding.FragmentPartialVisibleListBinding;
import com.androidpi.app.items.HeaderUnsplashPhoto;
import com.androidpi.app.viewholder.ErrorViewHolder;
import com.androidpi.app.viewholder.UnsplashPhotoHeaderViewHolder;
import com.androidpi.app.viewholder.UnsplashPhotoListViewHolder;
import com.androidpi.app.viewholder.items.ErrorItem;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/8/28.
 */
@BindLayout(R.layout.fragment_partial_visible_list)
public class PartialVisibleListFragment extends BaseFragment<FragmentPartialVisibleListBinding> {
    private UnsplashViewModel unsplashViewModel;
    private RecyclerAdapter adapter;
    private HeaderUnsplashPhoto headerUnsplashPhoto = new HeaderUnsplashPhoto(null);

    public static UnsplashPhotoListFragment newInstance() {

        Bundle args = new Bundle();

        UnsplashPhotoListFragment fragment = new UnsplashPhotoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoHeaderViewHolder.class, UnsplashPhotoListViewHolder.class, ErrorViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefreshContentBehavior behavior = LiteRefreshHelper.getAttachedBehavior(binding.recyclerView);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (listResource == null)
                    return;
                UnsplashPhotoPage page = listResource.data;
                if (page == null)
                    return;
                if (listResource.isSuccess()) {
                    behavior.refreshComplete();
                    if (page.isFirstPage()) {
                        adapter.setPayloads(headerUnsplashPhoto);
                        adapter.addPayloads(page.getPhotos());
                    } else {
                        adapter.addPayloads(page.getPhotos());
                    }
                } else if (listResource.isError()) {
                    if (page.isFirstPage()) {
                        behavior.refreshError(listResource.throwable);
                        adapter.setPayloads(new ErrorItem(listResource.throwable.getMessage()));
                    }
                }
            }
        });


        final float translationDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128, view.getResources().getDisplayMetrics());
        binding.circleProgress.resetStyle();
        binding.circleProgress.setProgress(1f);
        binding.circleProgress.setTranslationY(-translationDistance);

        if (behavior != null) {
            behavior.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onStartScroll(View view, int max, boolean isTouch) {
//                    Timber.d("onStartScroll: isTouch %b", isTouch);
                }

                @Override
                public void onScroll(View view, int current, int delta, int max, boolean isTouch) {
//                    Timber.d("onScroll: isTouch %b", isTouch);
                }

                @Override
                public void onStopScroll(View view, int current, int max, boolean isTouch) {
//                    Timber.d("onStopScroll: isTouch %b", isTouch);
                    if (isTouch && !behavior.getController().isRefreshing()) {
                        binding.circleProgress.resetStyle();
                        binding.circleProgress.setProgress(1f);
                        binding.circleProgress.animate().translationY(-translationDistance);
                    }
                }
            });

            behavior.addOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefreshStart() {
//                    Timber.d("onRefreshStart");
                    binding.circleProgress.resetStyle();
                    binding.circleProgress.setProgress(1f);
                }

                @Override
                public void onReleaseToRefresh() {
//                    Timber.d("onReleaseToRefresh");
                    binding.circleProgress.resetStyle();
                    binding.circleProgress.setProgress(1f);
                    binding.circleProgress.showCircle();
                    binding.circleProgress.animate().translationY(0);
                }

                @Override
                public void onRefresh() {
//                    Timber.d("onRefresh");
                    binding.circleProgress.resetStyle();
                    binding.circleProgress.startLoading();
                    unsplashViewModel.firstPage();
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
//                    Timber.d("onRefreshEnd");
                    binding.circleProgress.stopLoading();
                    binding.circleProgress.resetStyle();
                    binding.circleProgress.setProgress(1f);
                    binding.circleProgress.animate().translationY(-translationDistance);
                }
            });
        }
    }
}
