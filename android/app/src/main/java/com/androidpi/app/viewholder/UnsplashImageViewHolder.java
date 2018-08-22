package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderUnsplashImageBinding;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.view_holder_unsplash_image, dataTypes = ResUnsplashPhoto.class)
public class UnsplashImageViewHolder extends BaseViewHolder<ViewHolderUnsplashImageBinding> {

    public UnsplashImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        ResUnsplashPhoto resUnsplashPhoto = (ResUnsplashPhoto) data;
        binding.ivImage.loadUnsplashPhoto(resUnsplashPhoto);
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
    }
}
