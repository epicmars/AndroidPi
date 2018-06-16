package com.androidpi.app.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.androidpi.app.buiness.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jastrelax on 2017/11/7.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun provideNewsViewModel(newsViewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    abstract fun provideTodoViewModel(todoViewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoEditViewModel::class)
    abstract fun provideTodoEditViewModel(todoEditViewModel: TodoEditViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    abstract fun provideNotesViewModel(notesViewModel: NotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TextNoteEditViewModel::class)
    abstract fun provideTextNoteEditViewModel(textNoteEditViewModel: TextNoteEditViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HtmlReaderViewModel::class)
    abstract fun provideHtmlReaderViewModel(htmlReaderViewModel: HtmlReaderViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}