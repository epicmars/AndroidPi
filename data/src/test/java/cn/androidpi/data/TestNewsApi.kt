package cn.androidpi.data

import cn.androidpi.common.networks.http.RetrofitClientFactory
import cn.androidpi.data.remote.api.NewsApi
import cn.androidpi.data.remote.dto.ResNews
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit

/**
 * Created by jastrelax on 2017/11/3.
 */

@RunWith(JUnit4::class)
class TestNewsApi {

    var retrofit: Retrofit? = null
    var newsApi: NewsApi? = null

    @Before
    fun init() {
        retrofit = RetrofitClientFactory.newsHttpClient(BuildConfig.NEWS_BASE_URL)
        newsApi = retrofit?.create(NewsApi::class.java)
    }

    @Test
    fun testGetNews() {
        newsApi?.getNews()
                ?.subscribe(object : SingleObserver<List<ResNews>> {

                    override fun onError(e: Throwable?) {
                        println("TestNewsApi.onError")
                        e?.printStackTrace()
                    }

                    override fun onSuccess(t: List<ResNews>?) {
                        println("TestNewsApi.onSuccess")
                        t?.let {
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
