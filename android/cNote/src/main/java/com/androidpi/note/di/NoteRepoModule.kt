package com.androidpi.note.di

import com.androidpi.note.repo.TextNoteRepo
import com.androidpi.note.repo.TodoRepo
import com.androidpi.note.repo.impl.TextNoteRepository
import com.androidpi.note.repo.impl.TodoRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module(includes = arrayOf(NoteDatabaseModule::class))
abstract class NoteRepoModule {

    @Binds
    @Singleton
    abstract fun bindTodoRepo(todoRepository: TodoRepository): TodoRepo

    @Binds
    @Singleton
    abstract fun provideTextNoteRepo(textNoteRepo: TextNoteRepository): TextNoteRepo

}