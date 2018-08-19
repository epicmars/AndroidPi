package com.androidpi.common.networks.http

import com.androidpi.common.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Created by jastrelax on 2018/8/13.
 */
internal object RetrofitHttpClient {

    val okHttpClient = buildClient()

    fun buildClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(debugLoggingInterceptor())
        }
        return builder.build()
    }

    fun debugLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger
                { message ->
                    Timber.v(message)
                }
        ).setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}