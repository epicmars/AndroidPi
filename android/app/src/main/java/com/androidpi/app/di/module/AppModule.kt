package com.androidpi.app.di.module

import android.content.Context
import com.androidpi.data.di.DataModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/7.
 */
@Module(includes = arrayOf(DataModule::class, ViewModelModule::class))
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return context
    }

}