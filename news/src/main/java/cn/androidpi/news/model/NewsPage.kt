package cn.androidpi.news.model

import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE

/**
 * Created by jastrelax on 2017/11/22.
 */

class NewsPage {
    var currentPage = 0
    var newsList: MutableList<News> = ArrayList()

    fun firstPage(news: List<News>) {
        newsList.clear()
        currentPage(0, news)
    }

    fun currentPage(page: Int, news: List<News>) {
        currentPage = page
        newsList.addAll(news)
    }

    fun currentPage(): List<News> {
        val start = currentPage * PAGE_SIZE
        return newsList.subList(start, start + PAGE_SIZE)
    }

    fun previousPages(): List<News> {
        val end = currentPage * PAGE_SIZE
        return newsList.subList(0, end)
    }

    fun isFirstPage() = currentPage == 0
}