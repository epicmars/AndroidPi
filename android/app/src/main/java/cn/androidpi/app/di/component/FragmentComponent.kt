package cn.androidpi.app.di.component

import cn.androidpi.app.di.scope.FragmentScope
import dagger.Subcomponent

/**
 * Created by jastrelax on 2017/11/8.
 */
@FragmentScope
@Subcomponent
interface FragmentComponent {

    fun viewModelComponent(): ViewModelComponent
}