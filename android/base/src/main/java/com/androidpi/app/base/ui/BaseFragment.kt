package com.androidpi.app.base.ui

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidpi.app.base.di.Injectable

import com.androidpi.common.tools.LeakCanaryHelper
import dagger.android.support.AndroidSupportInjection

/**
 * Created by jastrelax on 2017/9/8.
 */

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {

    lateinit var binding: VDB

    var bindLayout: BindLayout? = null

    override fun onAttach(context: Context?) {
        bindLayout = javaClass.getAnnotation(BindLayout::class.java)
        val injectable = javaClass.getAnnotation(Injectable::class.java)
        if (injectable != null) {
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindLayout ?: // A fragment without view.
                return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, bindLayout!!.value, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        LeakCanaryHelper.refWatcher?.watch(this)
    }

    protected fun <T : ViewModel> getViewModel(clazz: Class<T>) : T {
        return ViewModelProviders.of(this).get(clazz)
    }

    protected fun <T : ViewModel> getViewModelOfActivity(clazz: Class<T>) : T {
        return ViewModelProviders.of(activity!!).get(clazz)
    }
}
