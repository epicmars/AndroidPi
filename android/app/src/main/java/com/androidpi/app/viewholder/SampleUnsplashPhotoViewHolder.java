package com.androidpi.app.viewholder;

import android.view.View;

import com.androidpi.app.R;
import com.androidpi.app.base.ui.BaseViewHolder;
import com.androidpi.app.base.ui.BindLayout;
import com.androidpi.app.databinding.ViewHolderSampleUnsplashPhotoBinding;
import com.androidpi.app.viewholder.items.SampleUnsplashPhoto;
import com.androidpi.common.image.glide.GlideApp;

/**
 * Created by jastrelax on 2018/9/9.
 */
@BindLayout(value = R.layout.view_holder_sample_unsplash_photo, dataTypes = SampleUnsplashPhoto.class)
public class SampleUnsplashPhotoViewHolder extends BaseViewHolder<ViewHolderSampleUnsplashPhotoBinding>{

    public SampleUnsplashPhotoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        if (data instanceof SampleUnsplashPhoto) {
            SampleUnsplashPhoto unsplashPhoto = ((SampleUnsplashPhoto) data);
            binding.tvAuthor.setText(itemView.getResources()
                    .getString(R.string.unsplash_photo_format, unsplashPhoto.getAuthor()));
            GlideApp.with(itemView).load(unsplashPhoto.getPhotoResId()).into(binding.ivPhoto);
        }
    }
}
