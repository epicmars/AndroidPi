package com.androidpi.data.remote

import com.androidpi.data.remote.dto.ResMoviePage
import com.androidpi.data.remote.dto.ResTrendingPage
import com.androidpi.data.remote.dto.ResTvPage

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by jastrelax on 2018/9/7.
 */
interface TheMovieDbApi {

    companion object {

        val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        val IMAGE_SIZE = "w500"

        val BASE_URL_V3 = "https://api.themoviedb.org/3/"
        val API_KEY_V3 = "a03feee7a52470501a8df012ac49fc5b"
    }

    @GET("discover/movie")
    fun discoverMovie(@Query("primary_release_date.gte") primaryReleaseDateGte: String,
                      @Query("primary_release_date.lte") primaryReleaseDateLte: String,
                      @Query("api_key") apiKey: String = API_KEY_V3): Single<ResMoviePage>

    @GET("discover/tv")
    fun discoverTv(@Query("air_date.gte") airDateGte: String,
                   @Query("air_date.lte") airDateLte: String,
                   @Query("api_key") apiKey: String = API_KEY_V3): Single<ResTvPage>

    @GET("trending/{media_type}/{time_window}")
    fun trending(@Path("media_type") mediaType: String,
                 @Path("time_window") timeWindow: String,
                 @Query("api_key") apiKey: String = API_KEY_V3): Single<ResTrendingPage>

}
