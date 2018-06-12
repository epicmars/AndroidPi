package com.androidpi.app.di.scope

import javax.inject.Scope

/**
 * Scopes for dependencies injection with dagger2.
 * Created by jastrelax on 2017/11/8.
 */


/**
 * An scope for instances within the lifecycle of a activity.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope


/**
 * An scope for instances within lifecycle of a fragment.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope


/**
 * A scope for instances within view models.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope




