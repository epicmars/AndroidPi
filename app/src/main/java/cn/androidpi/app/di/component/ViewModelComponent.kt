package cn.androidpi.app.di.component

import android.arch.lifecycle.MutableLiveData
import cn.androidpi.app.di.module.ViewModelModule
import cn.androidpi.app.di.scope.ViewModelScope
import dagger.Subcomponent

/**
 * Created by jastrelax on 2017/11/7.
 */

@ViewModelScope
@Subcomponent(modules = arrayOf(ViewModelModule::class))
interface ViewModelComponent {

    fun mutableLiveData(): MutableLiveData<Any>
}
