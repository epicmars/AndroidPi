package com.androidpi.app.viewholder;

import android.text.TextUtils;
import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderMovieBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.TheMovieDbApi;
import com.androidpi.data.remote.dto.ResMoviePage;
import com.androidpi.data.remote.dto.ResTrendingPage;

/**
 * Created by jastrelax on 2018/9/7.
 */
@BindLayout(value = R.layout.view_holder_movie, dataTypes = {ResMoviePage.ResultsBean.class,
        ResTrendingPage.ResultsBean.class})
public class MovieViewHolder extends BaseViewHolder<ViewHolderMovieBinding> {

    public MovieViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        if (data instanceof ResMoviePage.ResultsBean) {
            ResMoviePage.ResultsBean result = ((ResMoviePage.ResultsBean) data);
            binding.tvTitle.setText(result.getTitle());
            binding.tvOverview.setText(result.getOverview());
            GlideApp.with(itemView)
                    .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                            TheMovieDbApi.Companion.getIMAGE_SIZE() +
                    result.getPosterPath())
                    .into(binding.ivPost);
        }

        if (data instanceof ResTrendingPage.ResultsBean) {
            ResTrendingPage.ResultsBean result = ((ResTrendingPage.ResultsBean) data);
            binding.tvTitle.setText(TextUtils.isEmpty(result.getTitle()) ? result.getName() : result.getTitle());
            binding.tvOverview.setText(result.getOverview());
            GlideApp.with(itemView)
                    .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                            TheMovieDbApi.Companion.getIMAGE_SIZE() +
                            result.getPosterPath())
                    .into(binding.ivPost);
        }
    }
}
