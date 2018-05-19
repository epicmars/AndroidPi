package cn.androidpi.app.ui.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ViewHolderNewsThreeImagesBinding
import cn.androidpi.app.ui.activity.HtmlActivity
import cn.androidpi.app.ui.base.BaseViewHolder
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.common.image.glide.GlideApp
import cn.androidpi.news.vo.NewsPage.Companion.IMAGE_NEWS_SIZE_THRESHOLD
import cn.androidpi.news.vo.NewsThreeImages

/**
 * Created by jastrelax on 2017/11/30.
 */
@BindLayout(value = R.layout.view_holder_news_three_images, dataTypes = arrayOf(NewsThreeImages::class))
class NewsThreeImagesViewHolder(itemView: View) : BaseViewHolder<ViewHolderNewsThreeImagesBinding>(itemView){

    override fun <T : Any?> onBindView(data: T, position: Int) {
        val newsWithImages = data as? NewsThreeImages
        if (newsWithImages == null)
            return
        val news = newsWithImages.mNews
        mBinding.tvTitle.text = news.title
        mBinding.tvPublishTime.text = news.publishTime

        if (news.images != null && news.images!!.size > IMAGE_NEWS_SIZE_THRESHOLD) {
            GlideApp.with(itemView).load(news.images!![0]).into(mBinding.ivImage1)
            GlideApp.with(itemView).load(news.images!![1]).into(mBinding.ivImage2)
            GlideApp.with(itemView).load(news.images!![2]).into(mBinding.ivImage3)
        }

        itemView.setOnClickListener {
            v: View? ->
            val action = Intent(HtmlActivity.ACTION_VIEW, Uri.parse(news.url))
            if (action.resolveActivity(itemView.context.packageManager) != null) {
                v?.context?.startActivity(action)
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        GlideApp.with(itemView).clear(mBinding.ivImage1)
        GlideApp.with(itemView).clear(mBinding.ivImage2)
        GlideApp.with(itemView).clear(mBinding.ivImage3)
        itemView.setOnClickListener(null)
    }
}