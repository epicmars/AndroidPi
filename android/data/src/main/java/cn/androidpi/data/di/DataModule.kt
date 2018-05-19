package cn.androidpi.data.di

import cn.androidpi.news.di.NewsRepoModule
import cn.androidpi.note.di.NoteRepoModule
import dagger.Module

/**
 * Created by jastrelax on 2018/4/28.
 */
@Module(includes = arrayOf(NewsRepoModule::class, NoteRepoModule::class))
class DataModule {

}
