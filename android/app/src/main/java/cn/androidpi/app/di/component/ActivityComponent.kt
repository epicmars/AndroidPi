package cn.androidpi.app.di.component

import cn.androidpi.app.di.scope.ActivityScope
import dagger.Subcomponent

/**
 * Created by jastrelax on 2017/11/8.
 */
@ActivityScope
@Subcomponent
interface ActivityComponent {

    fun fragmentComponent(): FragmentComponent

    fun viewModelComponent(): ViewModelComponent
}