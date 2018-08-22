package com.androidpi.app.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

import com.androidpi.app.R
import com.androidpi.common.image.glide.GlideApp
import com.androidpi.data.remote.dto.ResUnsplashPhoto
import kotlinx.android.synthetic.main.unsplash_photo_view.view.*

/**
 * Created by jastrelax on 2018/8/14.
 */
class UnsplashPhotoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {

    init {
        inflate(context, R.layout.unsplash_photo_view, this)
    }

    fun loadUnsplashPhoto(resUnsplashPhoto: ResUnsplashPhoto) {

        GlideApp.with(this)
                .load(resUnsplashPhoto.urls.small)
                .into(iv_photo)

        GlideApp.with(this)
                .load(resUnsplashPhoto.user.profileImage.small)
                .into(iv_profile)

        tv_username.setText(resUnsplashPhoto.user.name)
    }

}
