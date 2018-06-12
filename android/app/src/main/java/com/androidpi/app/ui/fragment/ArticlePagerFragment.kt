package com.androidpi.app.ui.fragment

import com.androidpi.app.R
import com.androidpi.app.databinding.FragmentArticlePagerBinding
import com.androidpi.app.ui.base.BaseFragment
import com.androidpi.app.ui.base.BindLayout

/**
 * Created by jastrelax on 2018/1/5.
 */
@BindLayout(R.layout.fragment_article_pager, injectable = false)
class ArticlePagerFragment : BaseFragment<FragmentArticlePagerBinding>() {

    companion object {
        fun newInstance() : ArticlePagerFragment {
            return ArticlePagerFragment()
        }
    }
}