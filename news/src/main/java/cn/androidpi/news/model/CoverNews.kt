package cn.androidpi.news.model

import cn.androidpi.common.datetime.DateTimeUtils
import cn.androidpi.news.entity.News
import java.util.*

/**
 * Created by jastrelax on 2017/11/28.
 */
class CoverNews(newsList: MutableList<News>) {

    var mNews = ArrayList<News>()

    init {
        val now = Date()
        for (news in newsList) {
            if (news.publishTime == null)
                continue
            if (DateTimeUtils.parseDateTime(news.publishTime!!) > now)
                continue
            this.mNews.add(news)
        }
    }

}