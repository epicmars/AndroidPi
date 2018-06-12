package com.androidpi.app.di.component

import com.androidpi.app.AndroidPiApp
import com.androidpi.app.di.module.ActivityInjectorModule
import com.androidpi.app.di.module.AppModule
import com.androidpi.app.di.module.FragmentInjectorModule
import com.androidpi.news.local.dao.NewsDao
import com.androidpi.news.remote.api.NewsApi
import com.androidpi.news.repo.NewsRepo
import com.androidpi.note.local.dao.TodoDao
import com.androidpi.note.repo.TodoRepo
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