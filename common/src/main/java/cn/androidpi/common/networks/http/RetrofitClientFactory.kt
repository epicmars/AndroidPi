package cn.androidpi.common.networks.http

import cn.androidpi.common.BuildConfig
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
        val builder = okHttpClientBuilder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(debugLoggingInterceptor())
        }
        return Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    fun okHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient().newBuilder()
    }

    fun debugLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Timber.v(message)
        }).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

}
