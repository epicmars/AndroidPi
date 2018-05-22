package cn.androidpi.app.ui.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ViewHolderNewsBinding
import cn.androidpi.app.ui.activity.HtmlActivity
import cn.androidpi.app.ui.base.BaseViewHolder
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.common.image.glide.GlideApp
import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/7.
 */

@BindLayout(value = R.layout.view_holder_news, dataTypes = arrayOf(News::class))
class NewsViewHolder(itemView: View) : BaseViewHolder<ViewHolderNewsBinding>(itemView) {

    override fun <T : Any?> onBindView(data: T, position: Int) {
        val news = data as? News
        mBinding.tvTitle.text = news?.title
        mBinding.tvPublishTime.text = news?.publishTime

        if (news != null && news.images != null && news.images!!.isNotEmpty()) {
            mBinding.ivImage.visibility = View.VISIBLE
            GlideApp.with(itemView).load(news.images!![0]).into(mBinding.ivImage)
        }

        itemView.setOnClickListener {
            v: View? ->
            val action = Intent(HtmlActivity.ACTION_VIEW, Uri.parse(news?.url))
            if (action.resolveActivity(itemView.context.packageManager) != null) {
                v?.context?.startActivity(action)
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        itemView.setOnClickListener(null)
        mBinding.ivImage.visibility = View.GONE
        GlideApp.with(itemView).clear(mBinding.ivImage)
    }
}