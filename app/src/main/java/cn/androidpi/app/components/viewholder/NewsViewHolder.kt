package cn.androidpi.app.components.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseViewHolder
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.databinding.ViewHolderNewsBinding
import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/7.
 */

@BindLayout(value = R.layout.view_holder_news, dataTypes = arrayOf(News::class))
class NewsViewHolder(itemView: View) : BaseViewHolder<ViewHolderNewsBinding>(itemView) {

    override fun <T : Any?> present(data: T) {
        val news = data as? News
        mBinding.tvTitle.text = news?.title

        itemView.setOnClickListener {
            v: View? ->
            val action = Intent(Intent.ACTION_VIEW, Uri.parse(news?.url))
            v?.context?.startActivity(action)
        }
    }
}