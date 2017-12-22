package cn.androidpi.app.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.FragmentNewsPagerBinding
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.news.model.NewsModel.Companion.portal_163
import cn.androidpi.news.model.NewsModel.Companion.portal_163_name
import cn.androidpi.news.model.NewsModel.Companion.portal_all_name
import cn.androidpi.news.model.NewsModel.Companion.portal_ifeng
import cn.androidpi.news.model.NewsModel.Companion.portal_ifeng_name
import cn.androidpi.news.model.NewsModel.Companion.portal_qq
import cn.androidpi.news.model.NewsModel.Companion.portal_qq_name

/**
 * Created by jastrelax on 2017/12/15.
 */
@BindLayout(R.layout.fragment_news_pager, injectable = false)
class NewsPagerFragment : BaseFragment<FragmentNewsPagerBinding>() {

    companion object {
        fun newInstance(): NewsPagerFragment {
            return NewsPagerFragment()
        }
    }

    lateinit var mAdapter: NewsPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = NewsPagerAdapter(childFragmentManager)
        mBinding.newsPager.adapter = mAdapter
        mBinding.newsPager.offscreenPageLimit = 2

        mBinding.pagerTabs.setTextColor(view.resources.getColor(android.R.color.primary_text_dark))
        mBinding.pagerTabs.tabIndicatorColor = view.resources.getColor(android.R.color.primary_text_dark)
    }

    class NewsPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

        val portals = arrayOf(null, portal_qq, portal_163, portal_ifeng)
//        val portals = arrayOfNulls<String>(1)
        val portal_names = arrayOf(portal_all_name, portal_qq_name, portal_163_name, portal_ifeng_name)

        override fun getItem(position: Int): Fragment {
            return NewsFragment.newInstance(portals[position])
        }

        override fun getCount(): Int {
            return portals.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return portal_names.get(position)
        }

    }
}