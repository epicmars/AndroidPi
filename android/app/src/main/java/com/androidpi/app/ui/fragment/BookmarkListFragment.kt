package com.androidpi.app.ui.fragment

import com.androidpi.app.R
import com.androidpi.app.databinding.FragmentBookmarkListBinding
import com.androidpi.app.ui.base.BaseFragment
import com.androidpi.app.ui.base.BindLayout

/**
 * Created by jastrelax on 2018/1/5.
 */
@BindLayout(R.layout.fragment_bookmark_list, injectable = false)
class BookmarkListFragment : BaseFragment<FragmentBookmarkListBinding>() {

    companion object {
        fun newInstance() : BookmarkListFragment {
            return BookmarkListFragment()
        }
    }
}