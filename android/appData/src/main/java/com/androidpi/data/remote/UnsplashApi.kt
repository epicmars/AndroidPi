package com.androidpi.data.remote

import com.androidpi.data.remote.dto.ResRandomPhotos

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Document of unsplash APIs:
 *
 *
 *
 * Created by jastrelax on 2018/8/13.
 */
interface UnsplashApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val ACCESS_KEY = "d945a361e65ebf42cefda32bef69ea1fd2fa4029231cfc133d66dca73ab985b6"
    }

    /**
     * https://unsplash.com/documentation#get-a-random-photo
     */
    @GET("/photos/random")
    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    fun randomPhotos(@Query("collections") collections: String? = null,
                     @Query("featured") featured: String? = null,
                     @Query("username") username: String? = null,
                     @Query("query") query: String? = null,
                     @Query("w") w: Long? = null,
                     @Query("h") h: Long? = null,
                     @Query("orientation") orientation: String? = null,
                     @Query("count") count: Int = 1): Single<List<ResRandomPhotos>>

}
