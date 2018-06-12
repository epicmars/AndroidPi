package com.androidpi.app.ui.base

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

    lateinit var mBinding: VDB

    protected var mBindLayout: BindLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mBindLayout = javaClass.getAnnotation(BindLayout::class.java)
        if (mBindLayout != null && mBindLayout!!.injectable) {
            AndroidSupportInjection.inject(this)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBindLayout ?: // A fragment without view.
                return super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, mBindLayout!!.value, container, false)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        LeakCanaryHelper.refWatcher?.watch(this)
    }
}
