package cn.androidpi.data

import cn.androidpi.news.local.dao.NewsDao
import cn.androidpi.news.remote.api.NewsApi
import cn.androidpi.news.remote.dto.ResNews
import cn.androidpi.news.repo.NewsRepo
import cn.androidpi.news.repo.impl.NewsRepository
import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.io.IOException

/**
 * Test cases for news repository.
 *
 * When testing the repositories, because smaller unit tests for Dao and Api already
 * existed. There is no need to do that again, they can be mocked here.
 *
 * @see TestNewsDao
 * @see TestNewsApi
 *
 * Created by jastrelax on 2017/11/5.
 */
class TestNewsRepo {

    var mNewsRepo: NewsRepo? = null
    var mNewsApi: NewsApi? = mock(NewsApi::class.java)
    var mNewsDao: NewsDao? = mock(NewsDao::class.java)

    @Before
    fun init() {
        var newsRepo = NewsRepository()
        newsRepo.newsApi = mNewsApi!!
        newsRepo.newsDao = mNewsDao!!
        mNewsRepo = newsRepo
    }

    @After
    @Throws(IOException::class)
    fun reclaim() {

    }

    @Test
    @Throws(Exception::class)
    fun testNewsRepo() {
        //
        val page = 0
        val count = PAGE_SIZE
        // Faek news response
        val fakeNewsResponse = arrayListOf<ResNews>()
        // Fake news
        val fakeNews = arrayListOf<News>()
        for(i in 1..count) {
            fakeNewsResponse.add(ResNews())
            fakeNews.add(News())
        }

        Mockito.`when`(mNewsApi!!.getNews(page, count))
                .thenReturn(Single.error(IOException("request failed")))
        Mockito.`when`(mNewsDao!!.getNews(page, count))
                .thenReturn(fakeNews)
        // getLatestNews should pass if server is down
        mNewsRepo!!.getLatestNews()
                .subscribe(object : SingleObserver<List<News>> {
                    override fun onSuccess(t: List<News>?) {
                        println("TestNewsRepo.onSuccess")
                        assertEquals(PAGE_SIZE, t!!.size)
                        // the news may return from local database if server is down
                        println(t)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        println("TestNewsRepo.onError")
                        throw e!!
                    }
                })
    }
}
