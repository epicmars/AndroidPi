package cn.androidpi.app.di.module

import android.arch.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides

/**
 * Created by jastrelax on 2017/11/7.
 */
@Module
class ViewModelModule {

    @Provides
    fun provideMutableLiveData(): MutableLiveData<Any> {
        return MutableLiveData()
    }
}