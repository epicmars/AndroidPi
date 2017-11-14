package cn.androidpi.app.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import cn.androidpi.app.viewmodel.NewsViewModel
import cn.androidpi.app.viewmodel.TodoEditViewModel
import cn.androidpi.app.viewmodel.TodoListViewModel
import cn.androidpi.app.viewmodel.ViewModelFactory
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
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}