package com.androidpi.app.base.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.androidpi.common.tools.LeakCanaryHelper
import dagger.android.support.AndroidSupportInjection

/**
 * Created by jastrelax on 2017/9/8.
 */

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {

    lateinit var binding: VDB

    var bindLayout: BindLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        bindLayout = javaClass.getAnnotation(BindLayout::class.java)
        if (bindLayout != null && bindLayout!!.injectable) {
            AndroidSupportInjection.inject(this)
        }
        super.onCreate(savedInstanceState)
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
}
