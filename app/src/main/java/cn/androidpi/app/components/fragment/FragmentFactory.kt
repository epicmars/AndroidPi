package cn.androidpi.app.components.fragment

import android.support.v4.app.Fragment
import java.lang.ref.WeakReference

/**
 * Created by jastrelax on 2017/11/14.
 */
object FragmentFactoryMap {

    val factoryMap = HashMap<String, WeakReference<FragmentFactory<Fragment>>>()

    fun put(key: String, value: FragmentFactory<Fragment>) {
        factoryMap.remove(key)
        factoryMap[key] = WeakReference(value)
    }

}


abstract class FragmentFactory<out Fragment> {
    abstract fun create(): Fragment
}