package com.androidpi.app.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.androidpi.app.R
import com.androidpi.app.base.BaseFragment
import com.androidpi.app.base.BindLayout
import com.androidpi.app.databinding.FragmentMainBinding

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_main)
class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {

        val NAV_IDS = mapOf(R.id.nav_news to 0,
                R.id.nav_articles to 1,
                R.id.nav_book to 2,
                R.id.nav_me to 3)

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.pager.offscreenPageLimit = 2
        mBinding.pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            val pageTitles = intArrayOf(R.string.news, R.string.todo, R.string.note, R.string.me)

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> NewsPagerFragment.newInstance()
                    1 -> ArticlePagerFragment.newInstance()
                    2 -> BookmarkListFragment.newInstance()
                    3 -> ProfileFragment.newInstance()
                    else -> TempFragment.newInstance("An error occurred.")
                }
            }

            override fun getCount(): Int {
                return pageTitles.size
            }
        }

        mBinding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mBinding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })

        mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            val current : Int? =  NAV_IDS[itemId]
            if (current == null) {
                false
            } else {
                mBinding.pager.currentItem = current
                true
            }
        }
    }

}