package cn.androidpi.app.di.module

import cn.androidpi.app.di.scope.ActivityScope
import cn.androidpi.app.ui.activity.*
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