package cn.androidpi.app.components.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.databinding.FragmentMainBinding

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_main)
class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.pager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            val pageTitles = intArrayOf(R.string.news, R.string.todo)

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> NewsFragment.newInstance()
                    1 -> TodoFragment.newInstance()
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
            when (itemId) {
                R.id.nav_news -> {
                    mBinding.pager.currentItem = 0
                    true
                }
                R.id.nav_todo -> {
                    mBinding.pager.currentItem = 1
                    true
                }
                else -> false
            }
        }
    }

}