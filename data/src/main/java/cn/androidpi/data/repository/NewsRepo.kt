package cn.androidpi.data.repository

import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE
import io.reactivex.Single

/**
 * 仓储模型，定义最上层的业务接口。
 * Created by jastrelax on 2017/11/2.
 */

interface NewsRepo {


    /**
     * 从服务端更新新闻
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun refreshNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>

    /**
     * 获取最新的新闻
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>

    /**
     * 获取新闻
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>
}