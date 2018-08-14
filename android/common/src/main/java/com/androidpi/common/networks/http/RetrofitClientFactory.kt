package com.androidpi.common.networks.http

import com.androidpi.common.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * Created by jastrelax on 2017/11/3.
 */

object RetrofitClientFactory {

    fun newRetrofit(baseUrl: String): Retrofit {
        return retrofitBuilder().baseUrl(baseUrl).build()
    }

    fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .client(RetrofitHttpClient.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

}
