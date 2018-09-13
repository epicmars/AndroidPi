package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.base.vm.vo.Resource;
import com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel;
import com.androidpi.app.databinding.FragmentMoviePagerBinding;
import com.androidpi.data.remote.dto.ResTrendingPage;
import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel.TRENDING_ALL;
import static com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel.TRENDING_MOVIE;
import static com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel.TRENDING_TV;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(R.layout.fragment_movie_pager)
public class MoviePagerFragment extends BaseFragment<FragmentMoviePagerBinding> {

    private static final int MAX_COVER_SIZE = 5;
    MoviePagerAdapter pagerAdapter;
    TheMovieDbViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerAdapter = new MoviePagerAdapter(getChildFragmentManager());
        viewModel = getViewModelOfActivity(TheMovieDbViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());

        binding.viewPager.setAdapter(pagerAdapter);
        binding.pagerTabs.setupWithViewPager(binding.viewPager);

        RefreshHeaderBehavior headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewHeader);
        viewModel.getDayTrendingAllResults().observe(this, new Observer<Resource<ResTrendingPage>>() {
            @Override
            public void onChanged(@Nullable Resource<ResTrendingPage> resTrendingPageResource) {
                if (resTrendingPageResource == null)
                    return;
                if (resTrendingPageResource.isSuccess()) {
                    List<ResTrendingPage.ResultsBean> results = resTrendingPageResource.data.getResults();
                    binding.imagePagerHeader.setImages(selectRandomCovers(results));
                    headerBehavior.refreshComplete();
                } else if (resTrendingPageResource.isError()) {
                    headerBehavior.refreshError(resTrendingPageResource.throwable);
                }
            }
        });

        headerBehavior.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

            }

            @Override
            public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                float percent = (current - initial) / (float) (trigger - initial);
                binding.circleProgress.setProgress(percent);
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
                refreshCovers();
            }

            @Override
            public void onRefreshEnd(@Nullable Throwable throwable) {
                binding.circleProgress.stopLoading();
            }
        });

        if (viewModel.getDayTrendingAllResults().getValue() == null) {
            headerBehavior.refresh();
        }
    }

    private List<ResTrendingPage.ResultsBean> selectRandomCovers(List<ResTrendingPage.ResultsBean> origin) {
        if (origin == null || origin.size() <= MAX_COVER_SIZE)
            return origin;
        int size = origin.size();
        Random random = new Random();
        List<ResTrendingPage.ResultsBean> selected = new ArrayList<>();
        while (selected.size() < MAX_COVER_SIZE) {
            ResTrendingPage.ResultsBean result = origin.get(random.nextInt(size));
            if (selected.contains(result))
                continue;
            selected.add(result);
        }
        return selected;
    }

    private void refreshCovers() {
        viewModel.getDayTrending(TRENDING_ALL, viewModel.getDayTrendingAllResults());
    }

    static class MoviePagerAdapter extends FragmentStatePagerAdapter {

        static String[] PAGE_TITLES = {"Movie", "Tv"};

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MovieTvFragment.newInstance(TRENDING_MOVIE);
                case 1:
                default:
                    return MovieTvFragment.newInstance(TRENDING_TV);
            }
        }

        @Override
        public int getCount() {
            return PAGE_TITLES.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }
    }
}
