package cn.androidpi.news.di

import cn.androidpi.common.networks.http.RetrofitClientFactory
import cn.androidpi.news.BuildConfig
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */

enum class ServerInfo {
    NEWS_SERVER, NOTE_SERVER
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ServerType(val value: ServerInfo)

@Module
class HttpModule {

    @Provides
    @Singleton
    @ServerType(ServerInfo.NEWS_SERVER)
    fun provideRetrofit(): Retrofit {
        return RetrofitClientFactory.newRetrofit(BuildConfig.NEWS_BASE_URL)
    }
}