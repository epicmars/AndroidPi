package com.androidpi.app.di.module

import com.androidpi.app.di.scope.FragmentScope
import com.androidpi.app.fragment.*
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