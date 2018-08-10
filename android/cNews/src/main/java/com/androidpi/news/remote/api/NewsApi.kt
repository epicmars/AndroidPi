package com.androidpi.news.remote.api

import com.androidpi.news.remote.dto.ResTopHeadlines
import io.reactivex.Single
import retrofit2.http.*

/**
 * 新闻服务API。
 * Created by jastrelax on 2017/11/2.
 */
interface NewsApi {

    @GET("/v2/top-headlines")
    fun topHeadlines(@Query("country") country: String = "cn",
                     @Query("category") category: String? = null,
                     @Query("sources") sources: String? = null,
                     @Query("q") q: String? = null,
                     @Query("pageSize") pageSize: Int,
                     @Query("page") page: Int,
                     @Query("apiKey") apiKey: String = "5b7bf07986684b238a01fac5a5dbf19f"): Single<ResTopHeadlines>
}