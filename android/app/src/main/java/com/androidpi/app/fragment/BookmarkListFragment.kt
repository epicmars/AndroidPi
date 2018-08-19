package com.androidpi.app.fragment

import com.androidpi.app.R
import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.FragmentBookmarkListBinding

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