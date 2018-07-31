package com.androidpi.note.di

import android.arch.persistence.room.Room
import android.content.Context
import com.androidpi.note.local.NOTE_MIGRATION_1_2
import com.androidpi.note.local.NOTE_MIGRATION_2_3
import com.androidpi.note.local.NOTE_MIGRATION_3_4
import com.androidpi.note.local.NoteDatabase
import com.androidpi.note.local.dao.TextNoteDao
import com.androidpi.note.local.dao.TodoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module
class NoteDatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
                .addMigrations(NOTE_MIGRATION_1_2,
                        NOTE_MIGRATION_2_3,
                        NOTE_MIGRATION_3_4)
                .build()
    }


    @Provides
    @Singleton
    fun provideTodoDao(noteDb: NoteDatabase): TodoDao {
        return noteDb.todoDao()
    }

    @Provides
    @Singleton
    fun provideTextNoteDao(noteDb: NoteDatabase): TextNoteDao {
        return noteDb.textNoteDao()
    }


}