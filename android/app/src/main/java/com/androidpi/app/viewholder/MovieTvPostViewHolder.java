package com.androidpi.app.viewholder;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.activity.TemplateActivity;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderMovieTvPostBinding;
import com.androidpi.app.fragment.FragmentFactory;
import com.androidpi.app.fragment.MovieTvDetailFragment;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.TheMovieDbApi;
import com.androidpi.data.remote.dto.ResMoviePage;
import com.androidpi.data.remote.dto.ResTrendingPage;
import com.androidpi.data.remote.dto.ResTvPage;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(value = R.layout.view_holder_movie_tv_post, dataTypes = {
        ResTrendingPage.ResultsBean.class,
        ResTvPage.ResultsBean.class,
        ResMoviePage.ResultsBean.class
})
public class MovieTvPostViewHolder extends BaseViewHolder<ViewHolderMovieTvPostBinding> {

    public MovieTvPostViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        String postTitle = null;
        String postUrl = null;

        if (data instanceof ResMoviePage.ResultsBean) {
            ResMoviePage.ResultsBean result = ((ResMoviePage.ResultsBean) data);
            postTitle = result.getTitle();
            postUrl = result.getPosterPath();
        }

        if (data instanceof ResTvPage.ResultsBean) {
            ResTvPage.ResultsBean result = ((ResTvPage.ResultsBean) data);
            postTitle = result.getName();
            postUrl = result.getPosterPath();
        }

        if (data instanceof ResTrendingPage.ResultsBean) {
            ResTrendingPage.ResultsBean result = ((ResTrendingPage.ResultsBean) data);
            postTitle = TextUtils.isEmpty(result.getTitle()) ? result.getName() : result.getTitle();
            postUrl = result.getPosterPath();
        }
        binding.tvNameOrTitle.setText(postTitle);
        GlideApp.with(itemView)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        postUrl)
                .into(binding.ivPost);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateActivity.Companion.startWith(itemView.getContext(), 0, MovieTvDetailFragment.class.getName(), new FragmentFactory<Fragment>() {
                    @Override
                    public Fragment create() {
                        return MovieTvDetailFragment.newInstance((Parcelable) data);
                    }
                });
            }
        });
    }
}
