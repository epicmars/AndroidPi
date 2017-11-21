package cn.androidpi.data.di

import android.arch.persistence.room.Room
import android.content.Context
import cn.androidpi.data.local.NEWS_MIGRATION_1_2
import cn.androidpi.data.local.NOTE_MIGRATION_1_2
import cn.androidpi.data.local.NewsDatabase
import cn.androidpi.data.local.NoteDatabase
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.local.dao.TodoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
                .addMigrations(NOTE_MIGRATION_1_2)
                .build()
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, NewsDatabase.DATABASE_NAME)
                .addMigrations(NEWS_MIGRATION_1_2)
                .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(newsDb: NewsDatabase): NewsDao {
        return newsDb.newsDao()
    }

    @Provides
    @Singleton
    fun provideTodoDao(noteDb: NoteDatabase): TodoDao {
        return noteDb.todoDao()
    }
}