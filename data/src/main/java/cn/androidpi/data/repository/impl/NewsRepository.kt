package cn.androidpi.data.repository.impl

import javax.inject.Inject

import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.news.entity.News
import cn.androidpi.news.repo.NewsRepo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

/**
 * Created by jastrelax on 2017/11/2.
 */

class NewsRepository : NewsRepo {

    @Inject
    internal var newsApi: NewsApi? = null

    @Inject
    internal var newsDao: NewsDao? = null

    override fun refreshNews(page: Int, count: Int): Single<List<News>> {

        return newsApi!!.getNews(page, count)
                .toObservable()
                .flatMap { resNews -> Observable.fromIterable(resNews) }
                .map { resNews -> resNews.toNews() }
                .toList()
                .doOnSuccess {
                    newsDao?.insertNews(*it.toTypedArray())
                }
    }

    override fun getNews(page: Int, count: Int): Single<List<News>> {
        return Single.fromCallable{
            newsDao!!.getNews(page, count)
        }
    }

    override fun getLatestNews(page: Int, count: Int): Single<List<News>> {
        // Obey the "Single source of truth", always get news from database.
        // @see https://developer.android.com/topic/libraries/architecture/guide.html

        // When get latest news, [refreshNews] and zip with the data stream from local
        // database, and get news from local too when request failed.
        return refreshNews(page, count)
                .zipWith(getNews(page, count), BiFunction {
                    t1: List<News>, t2: List<News> -> t2
                })
                .onErrorResumeNext(getNews(page, count))
    }
}
