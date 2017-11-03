package cn.androidpi.data.repository.impl

import javax.inject.Inject

import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.news.entity.News
import cn.androidpi.news.repo.NewsRepo
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by jastrelax on 2017/11/2.
 */

class NewsRepository : NewsRepo {

    @Inject
    internal var newsApi: NewsApi? = null

    @Inject
    internal var newsDao: NewsDao? = null

    override fun getNews(page: Int, count: Int): Single<List<News>> {
        return newsApi!!.getNews(page, count)
                .toObservable()
                .flatMap { resNews -> Observable.fromIterable(resNews) }
                .map { resNews -> resNews.toNews() }
                .toList()
                .onErrorReturn { newsDao!!.getNews(page, count) }
    }
}
