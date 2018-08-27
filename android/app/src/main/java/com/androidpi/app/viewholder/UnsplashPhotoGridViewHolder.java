package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderUnsplashPhotoGridBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

/**
 * Created by jastrelax on 2018/8/13.
 */
@BindLayout(value = R.layout.view_holder_unsplash_photo_grid, dataTypes = ResUnsplashPhoto.class)
public class UnsplashPhotoGridViewHolder extends BaseViewHolder<ViewHolderUnsplashPhotoGridBinding> {

    public UnsplashPhotoGridViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        ResUnsplashPhoto resUnsplashPhoto = (ResUnsplashPhoto) data;
        loadUnsplashPhoto(resUnsplashPhoto);
    }

    private void loadUnsplashPhoto(ResUnsplashPhoto resUnsplashPhoto) {
        GlideApp.with(itemView)
                .load(resUnsplashPhoto.getUrls().getSmall())
                .into(binding.ivPhoto);

        GlideApp.with(itemView)
                .load(resUnsplashPhoto.getUser().getProfileImage().getSmall())
                .into(binding.ivProfile);

        binding.tvName.setText(resUnsplashPhoto.getUser().getName());
    }


}
