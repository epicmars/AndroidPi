package com.androidpi.data.di

import com.androidpi.news.di.NewsRepoModule
import com.androidpi.note.di.NoteRepoModule
import dagger.Module

/**
 * Created by jastrelax on 2018/4/28.
 */
@Module(includes = arrayOf(NewsRepoModule::class, NoteRepoModule::class))
class DataModule {

}
