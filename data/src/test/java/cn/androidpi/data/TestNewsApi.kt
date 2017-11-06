package cn.androidpi.data

import cn.androidpi.common.networks.http.RetrofitClientFactory
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.remote.dto.ResNews
import cn.androidpi.news.repo.NewsRepo
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import java.lang.Exception

/**
 * Created by jastrelax on 2017/11/3.
 */

class TestNewsApi {

    var retrofit: Retrofit? = null
    var newsApi: NewsApi? = null

    @Before
    fun init() {
        retrofit = RetrofitClientFactory.newsHttpClient(BuildConfig.NEWS_BASE_URL)
        newsApi = retrofit?.create(NewsApi::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testGetNews() {

        getNewsByPage(0)

        getNewsByPage(1)

    }

    @Throws(Exception::class)
    fun getNewsByPage(page: Int) {
        newsApi?.getNews(page, NewsRepo.PAGE_SIZE)
                ?.subscribe(object : SingleObserver<List<ResNews>> {

                    override fun onError(e: Throwable?) {
                        println("TestNewsApi.onError")
                        e?.printStackTrace()
                    }

                    override fun onSuccess(t: List<ResNews>?) {
                        println("TestNewsApi.onSuccess")
                        assertEquals(NewsRepo.PAGE_SIZE, t!!.size)
                        t!!.let {
                            for (news in t) {
                                println(news.toString())
                            }
                        }
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }
                })
    }
}
