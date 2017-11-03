package cn.androidpi.data.remote.api

import cn.androidpi.data.remote.dto.ResNews
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jastrelax on 2017/11/2.
 */
interface NewsApi {

    @GET("/api/v1/news/")
    fun getNews(@Query("page") page: Int = 0, @Query("count") count: Int = 12): Single<List<ResNews>>
}