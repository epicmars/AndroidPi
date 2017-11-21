package cn.androidpi.app.components.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.databinding.FragmentNewsCoverBinding
import cn.androidpi.news.entity.News
import com.bumptech.glide.Glide

/**
 * Created by jastrelax on 2017/11/21.
 */
@BindLayout(R.layout.fragment_news_cover, injectable = false)
class NewsCoverFragment : BaseFragment<FragmentNewsCoverBinding>() {

    companion object {
        val ARGS_NEWS = "NewsCoverFragment.ARGS_NEWS"

        fun newInstance(news: News): NewsCoverFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGS_NEWS, news)
            val fragment = NewsCoverFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val news: News? = arguments?.get(ARGS_NEWS) as News
        mBinding.tvTitle.text = news?.title
        Glide.with(this)
                .load(news?.images?.get(0))
                .into(mBinding.ivNewsCover)
    }
}


class NewsCoverPageAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var mNewsList = ArrayList<News>()

    fun setCoverNews(newsList: List<News>?) {
        if (newsList == null)
            return
        mNewsList.clear()
        mNewsList.addAll(newsList)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return NewsCoverFragment.newInstance(mNewsList.get(position))
    }

    override fun getCount(): Int {
        return mNewsList.size
    }
}