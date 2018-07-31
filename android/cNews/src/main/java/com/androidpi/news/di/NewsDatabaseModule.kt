package com.androidpi.news.di

import android.arch.persistence.room.Room
import android.content.Context
import com.androidpi.news.local.*
import com.androidpi.news.local.dao.BookmarkDao
import com.androidpi.news.local.dao.NewsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jastrelax on 2018/4/28.
 */
@Module
class NewsDatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, NewsDatabase.DATABASE_NAME)
                .addMigrations(NEWS_MIGRATION_1_2,
                        NEWS_MIGRATION_2_3,
                        NEWS_MIGRATION_3_4,
                        NEWS_MIGRATION_4_5)
                .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(newsDb: NewsDatabase): NewsDao {
        return newsDb.newsDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(newsDb: NewsDatabase): BookmarkDao {
        return newsDb.bookmarkDao()
    }
}