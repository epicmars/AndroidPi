//package com.androidpi.app.viewholder
//
//import android.content.Intent
//import android.net.Uri
//import android.view.View
//import com.androidpi.app.R
//import com.androidpi.app.activity.HtmlActivity
//import com.androidpi.app.base.BaseViewHolder
//import com.androidpi.app.base.BindLayout
//import com.androidpi.app.databinding.ViewHolderNewsThreeImagesBinding
//import com.androidpi.common.image.glide.GlideApp
//import com.androidpi.news.vo.NewsPage.Companion.IMAGE_NEWS_SIZE_THRESHOLD
//import com.androidpi.news.vo.NewsThreeImages
//
///**
// * Created by jastrelax on 2017/11/30.
// */
//@BindLayout(value = R.layout.view_holder_news_three_images, dataTypes = arrayOf(NewsThreeImages::class))
//class NewsThreeImagesViewHolder(itemView: View) : BaseViewHolder<ViewHolderNewsThreeImagesBinding>(itemView){
//
//    override fun <T : Any?> onBind(data: T, position: Int) {
//        val newsWithImages = data as? NewsThreeImages
//        if (newsWithImages == null)
//            return
//        val news = newsWithImages.mNews
//        mBinding.tvTitle.text = news.title
//        mBinding.tvPublishTime.text = news.publishTime
//
//        if (news.images != null && news.images!!.size > IMAGE_NEWS_SIZE_THRESHOLD) {
//            GlideApp.with(itemView).load(news.images!![0]).into(mBinding.ivImage1)
//            GlideApp.with(itemView).load(news.images!![1]).into(mBinding.ivImage2)
//            GlideApp.with(itemView).load(news.images!![2]).into(mBinding.ivImage3)
//        }
//
//        itemView.setOnClickListener {
//            v: View? ->
//            val action = Intent(HtmlActivity.ACTION_VIEW, Uri.parse(news.url))
//            if (action.resolveActivity(itemView.context.packageManager) != null) {
//                v?.context?.startActivity(action)
//            }
//        }
//    }
//
//    override fun onViewRecycled() {
//        super.onViewRecycled()
//        GlideApp.with(itemView).clear(mBinding.ivImage1)
//        GlideApp.with(itemView).clear(mBinding.ivImage2)
//        GlideApp.with(itemView).clear(mBinding.ivImage3)
//        itemView.setOnClickListener(null)
//    }
//}