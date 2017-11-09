package cn.androidpi.app.di.component

import cn.androidpi.app.AndroidPiApp
import cn.androidpi.app.di.module.ActivityInjectorModule
import cn.androidpi.app.di.module.AppModule
import cn.androidpi.app.di.module.FragmentInjectorModule
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.local.dao.TodoDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.data.repository.TodoRepo
import dagger.Component
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/7.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class,
        ActivityInjectorModule::class,
        FragmentInjectorModule::class))
interface AppComponent {

    fun newsApi(): NewsApi

    fun newsDao(): NewsDao

    fun todoDao(): TodoDao

    fun newsRepo(): NewsRepo

    fun todoRepo(): TodoRepo

    fun inject(app: AndroidPiApp)

    // subcomponents

    fun activityComponent(): ActivityComponent
}