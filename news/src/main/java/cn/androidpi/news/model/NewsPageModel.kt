package cn.androidpi.news.model

import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/12/16.
 */
class NewsPageModel() {

    var lastCachedPageNum: String? = null

    var newsList: MutableList<News> = mutableListOf()

    var page: Int? = null

    constructor(page: Int, list: List<News>): this() {
        this.page = page
        this.newsList.addAll(list)
    }
}