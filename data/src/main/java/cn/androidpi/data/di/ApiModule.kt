package cn.androidpi.data.di

import cn.androidpi.data.remote.api.NewsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/8.
 */
@Module(includes = arrayOf(HttpModule::class))
class ApiModule {

    @Provides
    @Singleton
    fun newsApi(@ServerType(ServerInfo.NEWS_SERVER) retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }
}