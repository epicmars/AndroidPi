package cn.androidpi.app.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.FragmentMainBinding
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_main)
class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {

        val NAV_IDS = mapOf(R.id.nav_news to 0,
                R.id.nav_articles to 1,
                R.id.nav_book to 2)

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.pager.offscreenPageLimit = 2
        mBinding.pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            val pageTitles = intArrayOf(R.string.news, R.string.todo, R.string.note)

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> NewsPagerFragment.newInstance()
                    1 -> ArticlePagerFragment.newInstance()
                    2 -> BookmarkListFragment.newInstance()
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