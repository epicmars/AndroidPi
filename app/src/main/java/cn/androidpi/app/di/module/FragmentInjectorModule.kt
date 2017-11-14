package cn.androidpi.app.di.module

import cn.androidpi.app.components.fragment.MainFragment
import cn.androidpi.app.components.fragment.NewsFragment
import cn.androidpi.app.components.fragment.TodoFragment
import cn.androidpi.app.components.fragment.TodoListFragment
import cn.androidpi.app.di.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module
abstract class FragmentInjectorModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMainFragmentInjector(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNewsFragmentInjector(): NewsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeTodoFragmentInjector(): TodoFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeTodoListFragmentInjector(): TodoListFragment
}