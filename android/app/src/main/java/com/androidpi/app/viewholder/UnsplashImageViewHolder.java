package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderUnsplashImageBinding;
import com.androidpi.common.image.glide.GlideApp;
import com.androidpi.data.remote.dto.ResUnsplashPhoto;

/**
 * Created by jastrelax on 2018/8/22.
 */
@BindLayout(value = R.layout.view_holder_unsplash_image, dataTypes = ResUnsplashPhoto.class)
public class UnsplashImageViewHolder extends BaseViewHolder<ViewHolderUnsplashImageBinding> {

    public UnsplashImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        if (data instanceof ResUnsplashPhoto) {
            ResUnsplashPhoto resUnsplashPhoto = (ResUnsplashPhoto) data;
            binding.tvName.setText(resUnsplashPhoto.getUser().getName());
            GlideApp.with(itemView)
                    .load(resUnsplashPhoto.getUrls().getRegular())
                    .into(binding.ivProfile);
            GlideApp.with(itemView)
                    .load(resUnsplashPhoto.getUrls().getRegular())
                    .into(binding.ivImage);
        }
    }
}
