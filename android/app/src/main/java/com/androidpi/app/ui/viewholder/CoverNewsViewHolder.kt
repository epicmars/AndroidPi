package com.androidpi.app.ui.viewholder

import android.view.View
import com.androidpi.app.R
import com.androidpi.app.databinding.ViewHolderCoverNewsBinding
import com.androidpi.app.ui.base.BaseViewHolder
import com.androidpi.app.ui.base.BindLayout
import com.androidpi.app.ui.fragment.CoverNewsPageAdapter
import com.androidpi.news.vo.CoverNews

/**
 * Created by jastrelax on 2017/11/28.
 */
@BindLayout(value = R.layout.view_holder_cover_news, dataTypes = arrayOf(CoverNews::class))
class CoverNewsViewHolder(itemView: View) : BaseViewHolder<ViewHolderCoverNewsBinding>(itemView) {

    lateinit var mNewsCoverAdapter: CoverNewsPageAdapter

    override fun <T : Any?> onBindView(data: T, position: Int) {

        val coverNews = data as CoverNews

        // news cover pager
        mNewsCoverAdapter = CoverNewsPageAdapter(mFragmentManager, coverNews)
        mBinding.newsPager.adapter = mNewsCoverAdapter
        mBinding.newsPager.offscreenPageLimit = 2

        mNewsCoverAdapter.setCoverNews(coverNews.mNews)
    }
}