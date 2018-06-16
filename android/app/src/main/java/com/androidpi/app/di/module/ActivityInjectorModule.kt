package com.androidpi.app.di.module

import com.androidpi.app.activity.*
import com.androidpi.app.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module
abstract class ActivityInjectorModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeTodoEditActivityInjector(): TodoEditActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeTemplateActivityInjector(): TemplateActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeTextNoteEditActivityInjector(): TextNoteEditActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeHtmlActivity(): HtmlActivity
}