package cn.androidpi.news.vo

import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/22.
 */

class NewsPage {
    companion object {
        val MAX_COVER_SIZE = 5
        val IMAGE_NEWS_SIZE_THRESHOLD = 5
    }

    var mPage = 0
    var mCoverNews: MutableList<News> = ArrayList()
    var mPreviousPages: MutableList<Any> = ArrayList()
    var mCurrentPage: MutableList<Any> = ArrayList()
    var mAllNews: MutableList<News> = ArrayList()

    fun firstPage(newsList: List<News>) {
        if (newsList.isEmpty())
            return
        mPage = 0
        mCurrentPage.clear()
        mCoverNews.clear()
        mPreviousPages.clear()
        mAllNews.clear()

        mAllNews.addAll(newsList)
        val coverSize = Math.min(MAX_COVER_SIZE, newsList.size)
        val coverNewsList = newsList.subList(0, coverSize)
        val iter = coverNewsList.iterator() as MutableIterator
        iter.forEach { news ->
            if (news.images != null && news.images!!.isNotEmpty()) {
                mCoverNews.add(news)
                iter.remove()
            }
        }
        val coverNews = CoverNews.newInstance(mCoverNews)
        if (coverNews != null) {
            mCurrentPage.add(coverNews)
        }

        for (t in newsList) {
            if (t.images != null && t.images!!.size > IMAGE_NEWS_SIZE_THRESHOLD) {
                mCurrentPage.add(NewsThreeImages(t))
            } else {
                mCurrentPage.add(t)
            }
        }
    }

    fun getNextPageNum(): Int {
        return mPage + 1
    }

    fun nextPage(newsList: List<News>) {
        mPage++
        mPreviousPages.addAll(mCurrentPage)
        val iter = newsList.iterator() as MutableIterator
        iter.forEach { it ->
            if (mAllNews.contains(it))
                iter.remove()
        }
        mAllNews.addAll(newsList)
        mCurrentPage.clear()
        for (t in newsList) {
            if (t.images != null && t.images!!.size > IMAGE_NEWS_SIZE_THRESHOLD) {
                mCurrentPage.add(NewsThreeImages(t))
            } else {
                mCurrentPage.add(t)
            }
        }
    }

    fun isFirstPage() = mPage == 0
}