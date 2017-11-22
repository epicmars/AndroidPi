package cn.androidpi.app.components.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.databinding.FragmentCoverNewsBinding
import cn.androidpi.app.viewmodel.CoverNewsViewModel
import cn.androidpi.news.entity.News
import com.bumptech.glide.Glide

/**
 * Created by jastrelax on 2017/11/21.
 */

@BindLayout(R.layout.fragment_cover_news, injectable = false)
class CoverNewsFragment : BaseFragment<FragmentCoverNewsBinding>() {

    var mNews: News? = null

    var mPosition : Int? = null

    lateinit var mCoverNewsModel: CoverNewsViewModel

    companion object {
        val ARGS_POSITION = "CoverNewsFragment.ARGS_POSITION"

        fun newInstance(position: Int): CoverNewsFragment {
            val bundle = Bundle()
            bundle.putInt(ARGS_POSITION, position)
            val fragment = CoverNewsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPosition = arguments?.getInt(ARGS_POSITION)

        mCoverNewsModel = ViewModelProviders.of(activity!!).get(CoverNewsViewModel::class.java)

        mCoverNewsModel.mCoverNews.observe(this, object : Observer<List<News>> {
            override fun onChanged(t: List<News>?) {
                if (t == null || mPosition == null || mPosition!! >= t.size) {
                    return
                }
                mNews = t[mPosition!!]
                mBinding.tvTitle.text = mNews?.title
                Glide.with(view)
                        .load(mNews?.images?.get(0))
                        .into(mBinding.ivNewsCover)
            }
        })
    }
}

class CoverNewsPageAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    var mCoverNewsModel: CoverNewsViewModel? = null

    constructor(fm: FragmentManager?, coverNewsModel: CoverNewsViewModel?) : this(fm) {
        mCoverNewsModel = coverNewsModel
    }

    fun setCoverNews(newsList: List<News>?): Int {
        if (newsList == null || newsList.isEmpty())
            return 0
        mCoverNewsModel?.clear()
        for (news in newsList) {
            if (news.images == null || news.images!!.isEmpty())
                continue
            mCoverNewsModel?.add(news)
        }
        mCoverNewsModel?.update()
        notifyDataSetChanged()
        return mCoverNewsModel?.getSize()!!
    }

    override fun getItem(position: Int): Fragment {
        return CoverNewsFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return mCoverNewsModel?.getSize()!!
    }

    override fun getItemPosition(`object`: Any): Int {
        val fragment = `object` as CoverNewsFragment
        return if (fragment.mPosition == null) PagerAdapter.POSITION_NONE else fragment.mPosition!!
    }
}
