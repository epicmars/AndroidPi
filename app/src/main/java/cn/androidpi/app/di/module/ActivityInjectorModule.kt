package cn.androidpi.app.di.module

import cn.androidpi.app.components.activity.MainActivity
import cn.androidpi.app.components.activity.TemplateActivity
import cn.androidpi.app.components.activity.TodoEditActivity
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