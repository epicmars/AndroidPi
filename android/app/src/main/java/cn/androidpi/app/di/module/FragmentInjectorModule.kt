package cn.androidpi.app.di.module

import cn.androidpi.app.di.scope.FragmentScope
import cn.androidpi.app.ui.fragment.*
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

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNotesFragmentInjector(): NotesFragment
}