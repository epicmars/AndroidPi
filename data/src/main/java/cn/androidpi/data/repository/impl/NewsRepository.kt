package cn.androidpi.data.repository.impl

import cn.androidpi.common.datetime.DateTimeUtils
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.repository.NetworkBoundFlowable
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.news.entity.News
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/2.
 */

@Singleton
class NewsRepository @Inject constructor() : NewsRepo {

    @Inject
    lateinit var newsApi: NewsApi

    @Inject
    lateinit var newsDao: NewsDao

    override fun refreshNews(page: Int, count: Int): Single<List<News>> {
        return newsApi.getNews(page, count)
                .toObservable()
                .flatMap { resNews -> Observable.fromIterable(resNews) }
                .map { resNews -> resNews.toNews() }
                .toList()
    }

    override fun getNews(page: Int, count: Int, offset: Int): Single<List<News>> {
        return newsDao.getNews(page, count, offset)
    }

    override fun getLatestNews(page: Int, count: Int): Single<List<News>> {
        // Obey the "Single source of truth", always get news from database.
        // @see https://developer.android.com/topic/libraries/architecture/guide.html

        // When get latest news, [refreshNews] and zip with the data stream from local
        // database, and get news from local when request failed.
        return refreshNews(page, count)
                .toObservable()
                .flatMap { newsList ->
                    Observable.fromIterable(newsList)
                }
                .flatMap { news ->
                    Observable.fromCallable {
                        newsDao.insertNews(news)
                        news
                    }.onErrorReturnItem(news)
                }
                .toList()
                .zipWith(getNews(page, count), BiFunction { t1: List<News>, t2: List<News> ->
                    t2
                })
                .doOnError {
                    Timber.e(it)
                }
                .onErrorResumeNext(getNews(page, count))
    }

    override fun getLatestNews(page: Int, count: Int, offset: Int): Single<List<News>> {
        // 目前的获取策略
        // 第一页总是尝试从服务端获取后保存到本地
        // 第二页及其之后的页面首先从本地获取，然后根据本地页面判断是否请求网络
        // 判断策略为：
        // 1. 本地页面是否为空
        // 2. 是否较为陈旧(obsolete)，如果页面较新鲜(fresh)则不请求网络，根据上一页最后一项的时间来判断

        return object : NetworkBoundFlowable<List<News>>() {
            override fun loadFromDb(): Flowable<List<News>> {
                return getNews(page, count, offset).toFlowable()
            }

            override fun shouldFetch(dbResult: List<News>): Boolean {
                if (dbResult.isEmpty() || page == 0)
                    return true
                val firstNews = dbResult[0]
                if (firstNews.publishTime == null)
                    return true
                try {
                    val firstDate = DateTimeUtils.parseDateTime(firstNews.publishTime!!)
                    val lastNews = newsDao.getNewsBefore(firstNews.publishTime!!)
                    if (lastNews.publishTime == null)
                        return true
                    val lastDate = DateTimeUtils.parseDateTime(lastNews.publishTime!!)
                    return (lastDate.time - firstDate.time > 10 * DateTimeUtils.ONE_MINUTE_MS)
                } catch (e: Exception) {
                    return true
                }
            }

            override fun createCall(): Flowable<List<News>> {
                return refreshNews(page, count).toFlowable()
            }

            override fun saveCallResult(result: List<News>): Boolean {
                // if at least one insertion succeed then it's fresh
                var count = 0
                for (news in result) {
                    try {
                        newsDao.insertNews(news)
                    } catch (e: Exception) {
                        // ignore
                        count++
                    }
                }

                if (count > 0 && count == result.size) {
                    return false
                }
                return true
            }
        }.getResultAsFlowable().singleOrError()
    }
}
