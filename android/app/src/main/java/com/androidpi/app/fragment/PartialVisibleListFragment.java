package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.ui.RecyclerAdapter;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.buiness.viewmodel.UnsplashViewModel;
import com.androidpi.app.buiness.vo.UnsplashPhotoPage;
import com.androidpi.app.databinding.FragmentPartialVisibleListBinding;
import com.androidpi.app.items.HeaderUnsplashPhoto;
import com.androidpi.app.viewholder.ErrorViewHolder;
import com.androidpi.app.viewholder.LoadingViewHolder;
import com.androidpi.app.viewholder.UnsplashPhotoHeaderViewHolder;
import com.androidpi.app.viewholder.UnsplashPhotoListViewHolder;
import com.androidpi.app.viewholder.items.ErrorItem;
import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshContentBehavior;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

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
        adapter.register(UnsplashPhotoHeaderViewHolder.class,
                UnsplashPhotoListViewHolder.class,
                ErrorViewHolder.class,
                LoadingViewHolder.class);
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
                public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {
                }

                @Override
                public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                }

                @Override
                public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {
                    if (type == TYPE_TOUCH && !behavior.getController().isRefreshing()) {
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
                    binding.circleProgress.fillCircle();
                    binding.circleProgress.animate().translationY(0);
                }

                @Override
                public void onRefresh() {
//                    Timber.d("onRefresh");
                    if (binding.circleProgress.getTranslationY() != 0) {
                        binding.circleProgress.setTranslationY(0);
                    }
                    binding.circleProgress.startLoading();
                    unsplashViewModel.firstPage();
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
//                    Timber.d("onRefreshEnd");
                    binding.circleProgress.stopLoading();
                    binding.circleProgress.animate().translationY(-translationDistance);
                }
            });
        }

        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            behavior.refresh();
        }
    }
}
