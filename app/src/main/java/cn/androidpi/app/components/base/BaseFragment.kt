package cn.androidpi.app.components.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cn.androidpi.common.tools.LeakCanaryHelper
import dagger.android.support.AndroidSupportInjection

/**
 * Created by jastrelax on 2017/9/8.
 */

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {

    lateinit var mBinding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bindLayout = javaClass.getAnnotation(BindLayout::class.java) ?: // A fragment without view.
                return super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, bindLayout.value, container, false)
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
