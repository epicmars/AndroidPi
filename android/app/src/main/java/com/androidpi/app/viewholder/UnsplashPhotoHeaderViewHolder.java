package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderUnsplashPhotoHeaderBinding;
import com.androidpi.app.items.HeaderUnsplashPhoto;
import com.androidpi.common.image.glide.GlideApp;

/**
 * Created by jastrelax on 2018/8/28.
 */
@BindLayout(value = R.layout.view_holder_unsplash_photo_header, dataTypes = HeaderUnsplashPhoto.class)
public class UnsplashPhotoHeaderViewHolder extends BaseViewHolder<ViewHolderUnsplashPhotoHeaderBinding> {

    public UnsplashPhotoHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        GlideApp.with(itemView).load(R.mipmap.photo8).into(binding.ivPhoto);
    }
}
