package com.androidpi.app.viewholder;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.androidpi.app.R;
import com.androidpi.app.base.BaseViewHolder;
import com.androidpi.app.base.BindLayout;
import com.androidpi.app.databinding.ViewholderUnsplashImageBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.common.image.glide.GlideRequests;
import com.androidpi.data.remote.dto.ResRandomPhotos;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.viewholder_unsplash_image, dataTypes = ResRandomPhotos.class)
public class UnsplashImageViewHolder extends BaseViewHolder<ViewholderUnsplashImageBinding> {

    public UnsplashImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        ResRandomPhotos resRandomPhotos = (ResRandomPhotos) data;
        GlideApp.with(itemView).load(resRandomPhotos.getUrls().getSmall())
                .into(mBinding.ivImage);
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
    }
}
