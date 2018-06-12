package com.androidpi.app.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import com.androidpi.app.R
import com.androidpi.app.databinding.FragmentCoverNewsBinding
import com.androidpi.app.ui.activity.HtmlActivity
import com.androidpi.app.ui.base.BaseFragment
import com.androidpi.app.ui.base.BindLayout
import com.androidpi.common.image.glide.GlideApp
import com.androidpi.news.entity.News
import com.androidpi.news.vo.CoverNews

/**
 * Created by jastrelax on 2017/11/21.
 */

@BindLayout(R.layout.fragment_cover_news, injectable = false)
class CoverNewsFragment : BaseFragment<FragmentCoverNewsBinding>() {

    var mNews: News? = null

    var mPosition : Int? = null

    companion object {
        val ARGS_NEWS = "CoverNewsFragment.ARGS_NEWS"
        val ARGS_POSITION = "CoverNewsFragment.ARGS_POSITION"

        fun newInstance(position: Int, news: News): CoverNewsFragment {
            val bundle = Bundle()
            bundle.putInt(ARGS_POSITION, position)
            bundle.putParcelable(ARGS_NEWS, news)
            val fragment = CoverNewsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPosition = arguments?.getInt(ARGS_POSITION)
        mNews = arguments?.getParcelable(ARGS_NEWS)

        mBinding.tvTitle.text = mNews?.title
        mBinding.ivNewsCover.setOnClickListener {
            startActivity(Intent(HtmlActivity.ACTION_VIEW, Uri.parse(mNews?.url)))
        }
        GlideApp.with(this).load(mNews?.images?.get(0)).into(mBinding.ivNewsCover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        GlideApp.with(this).clear(mBinding.ivNewsCover)
    }
}

class CoverNewsPageAdapter(fm: FragmentManager?, news: CoverNews?) : FragmentStatePagerAdapter(fm) {

    var coverNews = ArrayList<News>()

    fun setCoverNews(newsList: List<News>?) {
        if (newsList == null || newsList.isEmpty())
            return
        for (news in newsList) {
            if (news.images == null || news.images!!.isEmpty())
                continue
            coverNews.add(news)
        }
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return CoverNewsFragment.newInstance(position, coverNews.get(position))
    }

    override fun getCount(): Int {
        return coverNews.size
    }

    override fun getItemPosition(`object`: Any): Int {
        val fragment = `object` as CoverNewsFragment
        return if (fragment.mPosition == null) PagerAdapter.POSITION_NONE else fragment.mPosition!!
    }
}
