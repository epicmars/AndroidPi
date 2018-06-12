package com.androidpi.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.androidpi.app.ui.base.BaseFragment
import com.androidpi.app.databinding.FragmentTempBinding

/**
 * Created by jastrelax on 2017/11/7.
 */
class TempFragment : BaseFragment<FragmentTempBinding>() {

    companion object {
        val KEY_MESSAGE = "cn.androidpi.app.components.fragment.TempFragment.KEY_MESSAGE"

        fun newInstance(message: String): TempFragment {

            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            val fragment = TempFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvMessage.text = arguments?.getString(KEY_MESSAGE)
    }
}