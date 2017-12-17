package cn.androidpi.data.remote.dto

import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.portal_163
import cn.androidpi.news.model.NewsModel.Companion.portal_ifeng
import cn.androidpi.news.model.NewsModel.Companion.portal_qq
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 新闻服务接口返回数据。
 * @see cn.androidpi.data.remote.api.NewsApi.getNews
 * Created by jastrelax on 2017/11/2.
 */

class ResNews {

    var _id: IdBean? = null
    var category: String? = null
    @SerializedName("origin_title")
    var originTitle: String? = null
    @SerializedName("publish_time")
    var publishTime: String? = null
    @SerializedName("source_name")
    var sourceName: String? = null
    @SerializedName("source_url")
    var sourceUrl: String? = null
    var title: String? = null
    var url: String? = null
    var keywords: List<String>? = null
    var images: Array<String>? = null

    class IdBean {

        @SerializedName("\$oid")
        var oid: String? = null
    }

    fun toNews(): News {

        var news = News()
        news.title = title
        news.newsId = _id?.oid
        news.category = category
        news.originTitle = originTitle
        news.publishTime = publishTime
        news.sourceName = sourceName
        news.sourceUrl = sourceUrl
        news.url = url
        news.keywords = keywords?.toTypedArray()
        news.images = images?.copyOf()

        if (url != null) {
            when {
                url!!.indexOf(portal_163) > 0 -> news.portal = portal_163
                url!!.indexOf(portal_qq) > 0 -> news.portal = portal_qq
                url!!.indexOf(portal_ifeng) > 0 -> news.portal = portal_ifeng
            }
        }
        return news
    }

    override fun toString(): String {
        return "ResNews(_id=$_id, category=$category, originTitle=$originTitle, publishTime=$publishTime, sourceName=$sourceName, sourceUrl=$sourceUrl, title=$title, url=$url, keywords=$keywords, images=${Arrays.toString(images)})"
    }


}
