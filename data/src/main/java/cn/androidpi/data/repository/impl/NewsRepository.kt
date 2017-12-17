package cn.androidpi.data.repository.impl

import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.repository.NetworkBoundFlowable
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsPageModel
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

    override fun getLatestNews(page: Int, count: Int, offset: Int): Single<NewsPageModel> {
        return getLatestNews(page, count, offset, null, null).singleOrError()
    }

    override fun getLatestNews(page: Int, count: Int, offset: Int, portal: String?, cachedPageNum: String?): Flowable<NewsPageModel> {
        // 目前的获取策略
        // 第一页总是尝试从服务端获取后保存到本地
        // 第二页及其之后的页面首先从本地获取，然后根据本地页面判断是否请求网络
        // 判断策略为：
        // 1. 本地页面是否为空
        // 2. 是否较为陈旧(obsolete)，如果页面较新鲜(fresh)则不请求网络

        val cache = getNewsPage(page, count, offset, portal).toFlowable()
        val remoteOrCache = object : NetworkBoundFlowable<NewsPageModel>() {
            override fun loadFromDb(): Flowable<NewsPageModel> {
                return getNewsPage(page, count, offset, portal).toFlowable()
            }

            override fun shouldFetch(dbResult: NewsPageModel): Boolean {
                // if local page is empty or it's the first page
                if (dbResult.newsList.isEmpty() || page == 0 || cachedPageNum == null) {
                    return true
                }
                try {
                    var isContinuous = true
                    for (news in dbResult.newsList) {
                        val pageNum = Integer.valueOf(news.context)
                        if (pageNum <= 0) {
                            isContinuous = false
                            break
                        }
                    }
                    return !isContinuous
                } catch (e: Exception) {
                    // ignore
                }
                return true
            }

            override fun createCall(): Flowable<NewsPageModel> {
                return refreshNews(page, count)
                        .map { t ->
                            NewsPageModel(t)
                        }.toFlowable()
            }

            override fun saveCallResult(result: NewsPageModel) {
                // if at least one insertion succeed then it's fresh
                val pageStr = page.toString()
                if (result.newsList.isEmpty()) {
                    return
                }
                for (news in result.newsList) {
                    val cachedNews = newsDao.findByNewsId(news.newsId!!)
                    if (cachedNews == null) {
                        news.context = pageStr
                        newsDao.insertNews(news)
                    } else {
                        if (cachedNews.context == null || Integer.valueOf(cachedNews.context) == 0) {
                            cachedNews.context = pageStr
                            newsDao.updateNews(cachedNews)
                        }
                    }
                }
            }

            override fun updatedDbResult(dbResult: NewsPageModel): Flowable<NewsPageModel> {
                val result = ArrayList<News>()
                val pageStr = page.toString()
                var lastCachedPageNum: String? = cachedPageNum
                for (i in 0 until dbResult.newsList.size) {
                    val news = dbResult.newsList[i]
                    try {
                        val pageNum = Integer.valueOf(news.context)
                        if (page == 0 || (page > 0 && pageNum > 0)) {
                            if (news.context != pageStr) {
                                news.context = pageStr
                                newsDao.updateNews(news)
                            }
                            lastCachedPageNum = news.context
                            result.add(news)
                        } else {
                            lastCachedPageNum = null
                            break
                        }
                    } catch (e: Exception) {
                        // ignore
                        lastCachedPageNum = null
                        break
                    }
                }
                val pageModel = NewsPageModel(result)
                pageModel.lastCachedPageNum = lastCachedPageNum
                return Flowable.just(pageModel)
            }
        }.getResultAsFlowable()

        if (page == 0) {
            return cache.concatWith(remoteOrCache)
        } else {
            return remoteOrCache
        }
    }

    override fun getNewsPage(page: Int, count: Int, offset: Int, portal: String?): Single<NewsPageModel> {
        if (portal == null) {
            return getNews(page, count, offset)
                    .map { t ->
                        NewsPageModel(t)
                    }
        } else {
            return getNews(page, count, offset, portal)
                    .map { t ->
                        NewsPageModel(t)
                    }
        }
    }
}
