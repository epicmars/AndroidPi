package cn.androidpi.app.di

import cn.androidpi.app.AndroidPiApp
import cn.androidpi.app.di.component.DaggerAppComponent
import cn.androidpi.app.di.module.AppModule

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