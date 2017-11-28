package cn.androidpi.app.di.module

import cn.androidpi.app.ui.activity.MainActivity
import cn.androidpi.app.ui.activity.TemplateActivity
import cn.androidpi.app.ui.activity.TodoEditActivity
import cn.androidpi.app.di.scope.ActivityScope
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
}