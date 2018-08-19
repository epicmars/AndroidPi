package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewholderUnsplashImageBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.dto.ResRandomPhotos;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.viewholder_unsplash_image, dataTypes = ResRandomPhotos.class)
public class UnsplashImageViewHolder extends BaseViewHolder<ViewholderUnsplashImageBinding> {

    public UnsplashImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        ResRandomPhotos resRandomPhotos = (ResRandomPhotos) data;
        GlideApp.with(itemView).load(resRandomPhotos.getUrls().getSmall())
                .into(binding.ivImage);
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
    }
}
