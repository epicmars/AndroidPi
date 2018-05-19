package cn.androidpi.news.di

import cn.androidpi.news.repo.BookmarkRepo
import cn.androidpi.news.repo.NewsRepo
import cn.androidpi.news.repo.impl.BookmarkRepository
import cn.androidpi.news.repo.impl.NewsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module(includes = arrayOf(NewsDatabaseModule::class, ApiModule::class))
abstract class NewsRepoModule {

    @Binds
    @Singleton
    abstract fun provideNewsRepo(newsRepository: NewsRepository): NewsRepo

    @Binds
    @Singleton
    abstract fun provideBookmarkRepo(bookmarkRepo: BookmarkRepository): BookmarkRepo
}