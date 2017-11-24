package cn.androidpi.news.model

import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE

/**
 * Created by jastrelax on 2017/11/22.
 */

class NewsPage {
    var mPage = 0
    var mNewsList: MutableList<News> = ArrayList()

    fun firstPage(news: List<News>) {
        mPage = 0
        mNewsList.clear()
        mNewsList.addAll(news)
    }

    fun getNextPageNum(): Int {
        return mPage + 1
    }

    fun nextPage(news: List<News>) {
        mPage++
        mNewsList.addAll(news)
    }

    fun getCurrentPage(): List<News> {
        val size = mNewsList.size
        val start = clamp(mPage * PAGE_SIZE, 0, size-1)
        val end = clamp(start + PAGE_SIZE, 0, size)
        return mNewsList.subList(start, end)
    }

    fun getPreviousPages(): List<News> {
        val end = clamp(mPage * PAGE_SIZE, 0, mNewsList.size)
        return mNewsList.subList(0, end)
    }

    fun isFirstPage() = mPage == 0

    fun clamp(index: Int, min: Int, max: Int): Int{
        return maxOf(min, minOf(index, max))
    }
}