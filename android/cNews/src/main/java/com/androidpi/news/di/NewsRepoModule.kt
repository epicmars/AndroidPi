package com.androidpi.news.di

import com.androidpi.news.repo.BookmarkRepo
import com.androidpi.news.repo.NewsRepo
import com.androidpi.news.repo.impl.BookmarkRepository
import com.androidpi.news.repo.impl.NewsRepository
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