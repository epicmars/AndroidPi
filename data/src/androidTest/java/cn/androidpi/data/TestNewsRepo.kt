package cn.androidpi.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.androidpi.common.networks.http.RetrofitClientFactory
import cn.androidpi.data.local.NewsDatabase
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.repository.impl.NewsRepository
import cn.androidpi.news.entity.News

import org.junit.Before
import org.junit.runner.RunWith

import cn.androidpi.news.repo.NewsRepo
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException

/**
 * Created by jastrelax on 2017/11/5.
 */
@RunWith(AndroidJUnit4::class)
class TestNewsRepo {

    var mNewsRepo: NewsRepo? = null
    var mRetrofit: Retrofit? = null
    var mNewsApi: NewsApi? = null
    var mNewsDb: NewsDatabase? = null

    @Before
    fun init() {
        val context = InstrumentationRegistry.getTargetContext()

        mRetrofit = RetrofitClientFactory.newsHttpClient(BuildConfig.NEWS_BASE_URL)
        mNewsApi = mRetrofit?.create(NewsApi::class.java)
        mNewsDb = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
                .allowMainThreadQueries().build()
//        mNewsDb = Room.databaseBuilder(context, NewsDatabase::class.java, "test_news.db")
//                .allowMainThreadQueries().build()
        var newsRepo = NewsRepository()
        newsRepo.newsApi = mNewsApi
        newsRepo.newsDao = mNewsDb?.newsDao()

        mNewsRepo = newsRepo
    }

    @After
    @Throws(IOException::class)
    fun reclaim() {
        mNewsDb!!.close()
    }

    @Test
    @Throws(Exception::class)
    fun testGetNews() {
        mNewsRepo!!.getNews()
                .subscribe(object : SingleObserver<List<News>> {
                    override fun onSuccess(t: List<News>?) {
                        println("TestNewsRepo.onSuccess")
                        assertEquals(NewsRepo.PAGE_SIZE, t!!.size)
                        // the news may return from local database if server is down
                        println(t.toTypedArray())
                        val newsDao = mNewsDb?.newsDao()
                        newsDao?.insertNews(*t.toTypedArray())

                        val newsInDb = newsDao!!.getNews()
                        assertEquals(NewsRepo.PAGE_SIZE, newsInDb.size)
                        println(newsInDb.toTypedArray())

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
