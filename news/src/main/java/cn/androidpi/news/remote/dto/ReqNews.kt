package cn.androidpi.news.remote.dto

/**
 * 新闻服务接口请求数据。
 * @see cn.androidpi.data.remote.api.NewsApi.getNews
 *
 * Created by jastrelax on 2017/11/2.
 */

data class ReqNews(var page: Int = 0, var count: Int = 10)
