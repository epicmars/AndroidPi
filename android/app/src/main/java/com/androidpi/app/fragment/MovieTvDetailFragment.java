package com.androidpi.app.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseFragment;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.FragmentMovieTvDetailBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.TheMovieDbApi;
import com.androidpi.data.remote.dto.ResMoviePage;
import com.androidpi.data.remote.dto.ResTrendingPage;
import com.androidpi.data.remote.dto.ResTvPage;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(R.layout.fragment_movie_tv_detail)
public class MovieTvDetailFragment extends BaseFragment<FragmentMovieTvDetailBinding>{

    private static final String ARGS_DETAIL = "MovieTvDetailFragment.DETAIL";

    public static MovieTvDetailFragment newInstance(Parcelable data) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_DETAIL, data);
        MovieTvDetailFragment fragment = new MovieTvDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null)
            return;
        Parcelable data = args.getParcelable(ARGS_DETAIL);
        if (data == null)
            return;
        String postTitle = null;
        String postUrl = null;
        String overview = null;

        if (data instanceof ResMoviePage.ResultsBean) {
            ResMoviePage.ResultsBean result = ((ResMoviePage.ResultsBean) data);
            postTitle = result.getTitle();
            postUrl = result.getPosterPath();
            overview = result.getOverview();
        }

        if (data instanceof ResTvPage.ResultsBean) {
            ResTvPage.ResultsBean result = ((ResTvPage.ResultsBean) data);
            postTitle = result.getName();
            postUrl = result.getPosterPath();
            overview = result.getOverview();
        }

        if (data instanceof ResTrendingPage.ResultsBean) {
            ResTrendingPage.ResultsBean result = ((ResTrendingPage.ResultsBean) data);
            postTitle = TextUtils.isEmpty(result.getTitle()) ? result.getName() : result.getTitle();
            postUrl = result.getPosterPath();
            overview = result.getOverview();
        }

        binding.tvNameOrTitle.setText(postTitle);
        binding.tvOverview.setText(overview);
        GlideApp.with(view)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        postUrl)
                .into(binding.ivPostBg);

        GlideApp.with(view)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        postUrl)
                .into(binding.ivPostSmall);
    }
}
