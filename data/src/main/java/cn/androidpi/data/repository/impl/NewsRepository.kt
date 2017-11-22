package cn.androidpi.data.repository.impl

import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.news.entity.News
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

    override fun getNews(page: Int, count: Int): Single<List<News>> {
        return Single.fromCallable{
            newsDao.getNews(page, count)
        }
    }

    override fun getLatestNews(page: Int, count: Int): Single<List<News>> {
        // Obey the "Single source of truth", always get news from database.
        // @see https://developer.android.com/topic/libraries/architecture/guide.html

        // When get latest news, [refreshNews] and zip with the data stream from local
        // database, and get news from local when request failed.
        return refreshNews(page, count)
                .doOnSuccess {
                    for (news in it) {
                        newsDao.insertNews(news)
                    }
                }
                .zipWith(getNews(page, count), BiFunction {
                    t1: List<News>, t2: List<News> -> t2
                })
                .doOnError {
                    Timber.e(it)
                }
                .onErrorResumeNext(getNews(page, count))
    }
}
