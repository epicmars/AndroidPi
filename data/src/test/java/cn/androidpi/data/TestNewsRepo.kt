package cn.androidpi.data

import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.remote.dto.ResNews
import cn.androidpi.data.repository.impl.NewsRepository
import cn.androidpi.news.entity.News
import cn.androidpi.news.repo.NewsRepo
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
 * @see https://developer.android.com/topic/libraries/architecture/guide.html
 * @see TestNewsApi
 *
 * When testing the repositories, because smaller unit tests for Dao and Api already
 * existed. There this no need to do that again, they can be mocked here.
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
        newsRepo.newsApi = mNewsApi
        newsRepo.newsDao = mNewsDao
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
        val count = NewsRepo.PAGE_SIZE
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
                        assertEquals(NewsRepo.PAGE_SIZE, t!!.size)
                        // the news may return from local database if server is down
                        println(t.toTypedArray())
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
