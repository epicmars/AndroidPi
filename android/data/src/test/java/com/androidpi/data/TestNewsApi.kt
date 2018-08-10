package com.androidpi.data

import com.androidpi.common.networks.http.RetrofitClientFactory
import com.androidpi.news.remote.api.NewsApi
import com.androidpi.news.remote.dto.ResNews
import com.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.BufferedSource
import okio.Okio
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets

/**
 * Test cases for news api.
 *
 * References from
 * [Android architecture guide](https://developer.android.com/topic/libraries/architecture/guide.html).
 * Which says "To make tests independent from outside world, actual network call
 * should be avoided".
 *
 * The architecture guide says we should not communicate with the remote server,
 * just testing locally. But in real life it seems don't make sense, sometimes the
 * team is not big enough, thus the responsibilities aren't divided clearly.
 *
 * But in theory, locality should be a golden rule of testability. Client must not
 * trust remote server, and testing the server is not part of the client's responsibility.
 *
 * A fake or mocked local http server can be created for testing the client.
 * There are some Helpful libraries, eg.
 * - [nanohttpd](https://github.com/NanoHttpd/nanohttpd)
 * - [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
 * - Local http server, using a script language like python or nodejs is effective.
 * - [MockRetrofit](https://github.com/square/retrofit/tree/master/retrofit-mock)
 *
 * The `Nanohttpd`, `Mockwebserver` or local http server should be used in the api
 * tests which can test the full http stack, thus the response message can be modified
 * as needed. While the `MockRetrofit` or mocked api objects can be used in repository
 * tests.
 *
 * @see TestNewsRepo
 */

class TestNewsApi {

    var mockWebServer: MockWebServer? = null
    var retrofit: Retrofit? = null
    var newsApi: NewsApi? = null

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        retrofit = RetrofitClientFactory.retrofitBuilder()
                .baseUrl(mockWebServer!!.url("/"))
                .build()
        newsApi = retrofit?.create(NewsApi::class.java)

        enqueueResponse("news.json")
    }

    @After
    fun stopServer() {
        mockWebServer!!.shutdown()
    }

    @Throws(IOException::class)
    fun enqueueResponse(jsonFile: String) {
        val input = javaClass.classLoader.getResourceAsStream("api-response/" + jsonFile)
        val source = Okio.buffer(Okio.source(input)) as BufferedSource
        // enqueue responses
        mockWebServer!!.enqueue(MockResponse()
                .setBody(source.readString(StandardCharsets.UTF_8)))
    }

    @Test
    @Throws(Exception::class)
    fun testGetFirstPage() {
        getNewsByPage(0)
    }

    @Throws(Exception::class)
    fun getNewsByPage(page: Int) {
        newsApi?.topHeadlines(page, PAGE_SIZE)
                ?.subscribe(object : SingleObserver<List<ResNews>> {

                    override fun onError(e: Throwable?) {
                        println("TestNewsApi.onError")
                        throw e!!
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
