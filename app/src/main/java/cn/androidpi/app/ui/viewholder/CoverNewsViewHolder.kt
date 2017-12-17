package cn.androidpi.app.ui.viewholder

import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ViewHolderCoverNewsBinding
import cn.androidpi.app.ui.base.BaseViewHolder
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.app.ui.fragment.CoverNewsPageAdapter
import cn.androidpi.news.model.CoverNews

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