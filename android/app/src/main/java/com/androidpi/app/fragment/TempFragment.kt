package com.androidpi.app.fragment

import android.os.Bundle
import android.view.View
import com.androidpi.app.base.ui.BaseFragment
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
        binding.tvMessage.text = arguments?.getString(KEY_MESSAGE)
    }
}