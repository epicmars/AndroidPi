package cn.androidpi.news.model

import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/22.
 */

class NewsPage {
    val MAX_COVER_SIZE = 5

    var mPage = 0
    var mOffset = 0
    var mCoverNews: MutableList<News> = ArrayList()
    var mPreviousPages: MutableList<News> = ArrayList()
    var mCurrentPage: MutableList<News> = ArrayList()

    fun firstPage(news: List<News>) {
        if (news.isEmpty())
            return
        mPage = 0
        mOffset = 0
        mCurrentPage.clear()
        mCoverNews.clear()
        mPreviousPages.clear()

        val coverSize = Math.min(MAX_COVER_SIZE, news.size)
        mCoverNews.addAll(news.subList(0, coverSize))
        if (coverSize < news.size) {
            mCurrentPage.addAll(news.subList(coverSize, news.size))
        }
    }

    fun getNextPageNum(): Int {
        return mPage + 1
    }

    fun nextPage(news: List<News>) {
        mPage++
        mPreviousPages.addAll(mCurrentPage)
        val iter = news.iterator() as MutableIterator
        val origin = news.size
        iter.forEach {
            it ->
            if (mPreviousPages.contains(it))
                iter.remove()
        }
        val current = news.size
        mOffset += origin - current
        mCurrentPage.clear()
        mCurrentPage.addAll(news)
    }

    fun isFirstPage() = mPage == 0
}