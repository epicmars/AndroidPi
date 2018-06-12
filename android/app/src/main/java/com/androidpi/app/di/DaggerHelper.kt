package com.androidpi.app.di

import com.androidpi.app.AndroidPiApp
import com.androidpi.app.di.component.DaggerAppComponent
import com.androidpi.app.di.module.AppModule

/**
 * Created by jastrelax on 2017/11/8.
 */
class DaggerHelper {

    companion object {
        fun inject(app: AndroidPiApp) {
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
                    .inject(app)
        }
    }
}