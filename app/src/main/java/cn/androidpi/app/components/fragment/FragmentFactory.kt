package cn.androidpi.app.components.fragment

import android.support.v4.app.Fragment

/**
 * Created by jastrelax on 2017/11/14.
 */
object FragmentFactoryMap {

    val factoryMap = HashMap<String, FragmentFactory<Fragment>>()

    fun put(key: String, value: FragmentFactory<Fragment>) {
        factoryMap.remove(key)
        factoryMap[key] = value
    }

}


abstract class FragmentFactory<out Fragment> {
    abstract fun create(): Fragment
}