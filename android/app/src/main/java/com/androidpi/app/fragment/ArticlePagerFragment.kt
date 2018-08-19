package com.androidpi.app.fragment

import com.androidpi.app.R
import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.FragmentArticlePagerBinding

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