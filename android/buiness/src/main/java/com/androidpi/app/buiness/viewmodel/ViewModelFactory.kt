package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * [JvmSuppressWildcards] will prevent the compiler from generating wildcards.
 *
 * See [https://stackoverflow.com/questions/44638878/binding-into-map-with-kclass-type].
 * and [https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-suppress-wildcards/index.html]
 *
 * Created by jastrelax on 2017/11/11.
 */
@Singleton
class ViewModelFactory @Inject constructor(
        private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var vmProvider = viewModelMap.get(modelClass)
        if (vmProvider == null) {
            for (provider in viewModelMap) {
                if (provider.key.isAssignableFrom(modelClass)) {
                    vmProvider = provider.value
                    break
                }
            }
        }
        if (vmProvider == null) {
            throw IllegalArgumentException("unknow model class: ${modelClass.canonicalName}")
        }
        return vmProvider.get() as T
    }
}