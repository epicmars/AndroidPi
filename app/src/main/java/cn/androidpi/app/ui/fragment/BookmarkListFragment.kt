package cn.androidpi.app.ui.fragment

import cn.androidpi.app.R
import cn.androidpi.app.databinding.FragmentBookmarkListBinding
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout

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