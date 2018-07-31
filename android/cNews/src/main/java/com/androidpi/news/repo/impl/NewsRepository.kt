package com.androidpi.news.repo.impl

import com.androidpi.news.entity.News
import com.androidpi.news.local.dao.NewsDao
import com.androidpi.news.remote.api.NewsApi
import com.androidpi.news.repo.NetworkBoundFlowable
import com.androidpi.news.repo.NewsRepo
import com.androidpi.news.vo.NewsContext
import com.androidpi.news.vo.NewsPagination
import com.androidpi.news.vo.NewsPortalContext
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

    override fun refreshNews(page: Int, count: Int, portal: String?): Single<List<News>> {
        return newsApi.getNews(page, count, portal)
                .toObservable()
                .flatMap { resNews -> Observable.fromIterable(resNews) }
                .map { resNews -> resNews.toNews() }
                .toList()
    }

    override fun getNews(page: Int, count: Int, offset: Int): Single<List<News>> {
        return newsDao.getNews(page, count, 0)
    }

    override fun getNews(page: Int, count: Int, offset: Int, portal: String): Single<List<News>> {
        return newsDao.getNews(page, count, 0, portal)
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

    override fun getLatestNews(page: Int, count: Int, offset: Int): Single<NewsPagination> {
        return getLatestNews(page, count, offset, null, null).singleOrError()
    }

    override fun getLatestNews(page: Int, count: Int, offset: Int, portal: String?, cachedPageNum: String?): Flowable<NewsPagination> {
        // 目前的获取策略
        // 第一页总是尝试从服务端获取后保存到本地
        // 第二页及其之后的页面首先从本地获取，然后根据本地页面判断是否请求网络
        // 判断策略为：
        // 1. 本地页面是否为空
        // 2. 是否较为陈旧(obsolete)，如果页面较新鲜(fresh)则不请求网络

        val cache = getNewsPage(page, count, offset, portal).toFlowable()
        val remoteOrCache = object : NetworkBoundFlowable<NewsPagination>() {

            var newsOffset = if (page == 0) 0 else offset
            override fun loadFromDb(): Flowable<NewsPagination> {
                return getNewsPage(page, count, newsOffset, portal).toFlowable()
            }

            override fun shouldFetch(dbResult: NewsPagination): Boolean {
                // if local page is empty or it's the first page
                if (dbResult.newsList.isEmpty() || page == 0 || cachedPageNum == null) {
                    return true
                }
                try {
                    var isContinuous = true
                    for (news in dbResult.newsList) {
                        val newsContext = NewsContext.fromJson(news.context)
                        val portalContext = newsContext?.getPortalContext(portal)
                        if (portalContext == null || portalContext.page <= 0) {
                            isContinuous = false
                            break
                        }
                    }
                    return !isContinuous
                } catch (e: Exception) {
                    // ignore
                    Timber.e(e)
                }
                return true
            }

            override fun createCall(): Flowable<NewsPagination> {
                return refreshNews(page, count, portal)
                        .map { t ->
                            NewsPagination(page, page + 1, t)
                        }.toFlowable()
            }

            override fun saveCallResult(result: NewsPagination) {
                // if at least one insertion succeed then it's fresh
                if (result.newsList.isEmpty()) {
                    return
                }
                for (news in result.newsList) {
                    val cachedNews = newsDao.findByNewsId(news.newsId!!)
                    if (cachedNews == null) {
                        val context = NewsContext()
                        val portalContext = NewsPortalContext(null, page)
                        context.addPortalContext(portalContext)
                        news.context = context.toJson()
                        newsDao.insertNews(news)
                    } else {
                        var context = NewsContext.fromJson(cachedNews.context)
                        var portalContext = context?.getPortalContext(portal)

                        if (portalContext == null) {
                            portalContext = NewsPortalContext(portal, page)
                            if (context == null) {
                                context = NewsContext()
                            }
                            context.addPortalContext(portalContext)
                            cachedNews.context = context?.toJson()
                            newsDao.updateNews(cachedNews)
                        } else if (portalContext.page != page) {
                            portalContext.page = page
                            cachedNews.context = context?.toJson()
                            newsDao.updateNews(cachedNews)
                        }
                    }
                }
            }

            override fun updatedDbResult(dbResult: NewsPagination): Flowable<NewsPagination> {
                val result = ArrayList<News>()
                var lastCachedPageNum: String? = ""
                for (i in 0 until dbResult.newsList.size) {
                    val news = dbResult.newsList[i]
                    var context = NewsContext.fromJson(news.context)
                    var portalContext = context?.getPortalContext(portal)

                    if (portalContext == null) {
                        lastCachedPageNum = null
                        portalContext = NewsPortalContext(portal, page)
                        if (context == null) {
                            context = NewsContext()
                        }
                        (context as NewsContext).addPortalContext((portalContext as NewsPortalContext))
                        news.context = (context as NewsContext).toJson()
                        newsDao.updateNews(news)
                    } else {
                        // 页面大于零时遇到页面为零的表示非连续页面
                        if (lastCachedPageNum != null) {
                            lastCachedPageNum = if (page > 0 && (portalContext as NewsPortalContext).page == 0) null else (portalContext as NewsPortalContext).page.toString()
                        }
                        // ensure always get latest news
                        if ((portalContext as NewsPortalContext).page != page) {
                            // if returned news page is not fresh set lastCachedPageNum to null
                            (portalContext as NewsPortalContext).page = if (lastCachedPageNum == null) 0 else page
                            news.context = context?.toJson()
                            newsDao.updateNews(news)
                        }
                    }
                    // user interface doesn't need this
                    news.context = null
                    result.add(news)
                }
                val pageModel = NewsPagination(page, page + 1, result)
                pageModel.lastCachedPageNum = lastCachedPageNum
                pageModel.offset = newsOffset
                return Flowable.just(pageModel)
            }
        }.getResultAsFlowable()

        if (page == 0 && cachedPageNum == "-1") {
            return cache.concatWith(remoteOrCache)
        } else {
            return remoteOrCache
        }
    }

    override fun getNewsPage(page: Int, count: Int, offset: Int, portal: String?): Single<NewsPagination> {
        if (portal == null) {
            return getNews(page, count, offset)
                    .map { t ->
                        NewsPagination(page, page + 1, t)
                    }
        } else {
            return getNews(page, count, offset, portal)
                    .map { t ->
                        NewsPagination(page, page + 1, t)
                    }
        }
    }
}
