package com.androidpi.app.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.androidpi.app.R
import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.FragmentNewsPagerBinding
import com.androidpi.news.model.NewsListModel

/**
 * Created by jastrelax on 2017/12/15.
 */
@BindLayout(R.layout.fragment_news_pager)
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
        binding.newsPager.adapter = mAdapter
        binding.newsPager.offscreenPageLimit = 2

        binding.pagerTabs.setTextColor(view.resources.getColor(android.R.color.primary_text_dark))
        binding.pagerTabs.tabIndicatorColor = view.resources.getColor(android.R.color.primary_text_dark)
    }

    /**
     * ViewPager的嵌套，FragmentStatePagerAdapter会导致PullDownRefreshBehavior崩溃
     */
    inner class NewsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        val portals = NewsListModel.categories.keys
        val portal_names = NewsListModel.categories.values

        override fun getItem(position: Int): Fragment {
            return NewsFragment.newInstance(portals.elementAt(position))
        }

        override fun getCount(): Int {
            return portals.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return getString(portal_names.elementAt(position))
        }

    }
}