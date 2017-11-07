package cn.androidpi.data

import cn.androidpi.common.networks.http.RetrofitClientFactory
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.remote.dto.ResNews
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.lang.Exception

/**
 * Test cases for news api.
 *
 * @see https://developer.android.com/topic/libraries/architecture/guide.html
 * @see TestNewsRepo
 *
 * To make tests independent from outside world, actual network call should be
 * avoided. A fake local server can be created for testing.
 * There are some Helpful libraries, eg.
 * [nanohttpd](https://github.com/NanoHttpd/nanohttpd)
 * [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
 * [MockRetrofit](https://github.com/square/retrofit/tree/master/retrofit-mock)
 *
 * The Nanohttpd and Mockwebserver should be used in the api tests which talks
 * directly with server, thus the response message can be modified as needed.
 *
 * The architecture guide said we should not communicate with the remote server,
 * just testing locally. But in real life it seems don't make sense.
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
        newsApi?.getNews(page, PAGE_SIZE)
                ?.subscribe(object : SingleObserver<List<ResNews>> {

                    override fun onError(e: Throwable?) {
                        println("TestNewsApi.onError")
                        e?.printStackTrace()
                    }

                    override fun onSuccess(t: List<ResNews>?) {
                        println("TestNewsApi.onSuccess")
                        assertEquals(PAGE_SIZE, t!!.size)
                        println(t)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }
                })
    }
}
