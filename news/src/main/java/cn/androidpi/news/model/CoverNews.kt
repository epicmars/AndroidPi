package cn.androidpi.news.model

import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/28.
 */
class CoverNews(news: MutableList<News>) {

    var mNews = mutableListOf<News>()

    init {
        this.mNews = news
    }

}