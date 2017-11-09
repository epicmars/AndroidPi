package cn.androidpi.data.di

import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.data.repository.TodoRepo
import cn.androidpi.data.repository.impl.NewsRepository
import cn.androidpi.data.repository.impl.TodoRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module(includes = arrayOf(DatabaseModule::class, ApiModule::class))
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepo(todoRepository: TodoRepository): TodoRepo

    @Binds
    @Singleton
    abstract fun provideNewsRepo(newsRepository: NewsRepository): NewsRepo
}