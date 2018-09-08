package com.androidpi.app.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.List;

import static com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel.TRENDING_ALL;
import static com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel.TRENDING_MOVIE;
import static com.androidpi.app.buiness.viewmodel.TheMovieDbViewModel.TRENDING_TV;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(R.layout.fragment_movie_pager)
public class MoviePagerFragment extends BaseFragment<FragmentMoviePagerBinding> {

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

        viewModel.getDayTrendingAllResults().observe(this, new Observer<Resource<ResTrendingPage>>() {
            @Override
            public void onChanged(@Nullable Resource<ResTrendingPage> resTrendingPageResource) {
                if (resTrendingPageResource == null)
                    return;
                if (resTrendingPageResource.isSuccess()) {
                    List<ResTrendingPage.ResultsBean> results = resTrendingPageResource.data.getResults();
                    binding.imagePagerHeader.setImages(results.size() >= 3 ? results.subList(0, 3) : results);
                }
            }
        });

        if (viewModel.getDayTrendingAllResults().getValue() == null) {
            viewModel.getDayTrending(TRENDING_ALL, viewModel.getDayTrendingAllResults());
        }
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
