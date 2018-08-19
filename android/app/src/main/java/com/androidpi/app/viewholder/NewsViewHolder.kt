package com.androidpi.app.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import com.androidpi.app.R
import com.androidpi.app.activity.HtmlActivity
import com.androidpi.app.base.ui.BaseViewHolder
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.ViewHolderNewsBinding
import com.androidpi.common.image.glide.GlideApp
import com.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/7.
 */

@BindLayout(value = R.layout.view_holder_news, dataTypes = arrayOf(News::class))
class NewsViewHolder(itemView: View) : BaseViewHolder<ViewHolderNewsBinding>(itemView) {

    override fun <T : Any?> onBind(data: T, position: Int) {
        val news = data as? News
        binding.tvTitle.text = news?.title
        binding.tvPublishTime.text = news?.publishedAt

        if (news != null && news.urlToImage != null) {
            binding.ivImage.visibility = View.VISIBLE
            GlideApp.with(itemView).load(news.urlToImage).into(binding.ivImage)
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
        binding.ivImage.visibility = View.GONE
        GlideApp.with(itemView).clear(binding.ivImage)
    }
}