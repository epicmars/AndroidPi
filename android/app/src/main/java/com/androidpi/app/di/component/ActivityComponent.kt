package com.androidpi.app.di.component

import com.androidpi.app.di.scope.ActivityScope
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