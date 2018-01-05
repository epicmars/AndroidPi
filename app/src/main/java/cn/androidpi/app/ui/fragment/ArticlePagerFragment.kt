package cn.androidpi.app.ui.fragment

import cn.androidpi.app.R
import cn.androidpi.app.databinding.FragmentArticlePagerBinding
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout

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