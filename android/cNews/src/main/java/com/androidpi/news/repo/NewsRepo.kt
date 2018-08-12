package com.androidpi.news.repo

import com.androidpi.news.entity.News
import com.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import com.androidpi.news.vo.NewsPagination
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * 仓储模型，定义最上层的业务接口。
 * Created by jastrelax on 2017/11/2.
 */

interface NewsRepo {


    /**
     * 从服务端更新新闻，并保存到本地
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun refreshNews(page: Int = 1, count: Int = PAGE_SIZE, category: String? = "general"): Single<List<News>>

//    /**
//     * 获取最新的新闻，首先从服务端更新并保存到本地后，在从本地取出。如果从服务端获取失败了，
//     * 直接从本地取出。
//     *
//     * @param page   页数，默认为第一页
//     * @param count  每页新闻数量，默认大小为12篇
//     */
//    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>
//
//    /**
//     * 获取新闻，直接从本地取出已保存的新闻。
//     *
//     * @param page   页数，默认为第一页
//     * @param count  每页新闻数量，默认大小为12篇
//     */
//    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0): Single<List<News>>
//
//    /**
//     * 获取新闻，直接从本地取出已保存的新闻。
//     *
//     * @param page   页数，默认为第一页
//     * @param count  每页新闻数量，默认大小为12篇
//     */
//    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, category: String): Single<List<News>>
//
//    /**
//     * 获取新闻。
//     *
//     * @param page   页数，默认为第一页
//     * @param count  每页新闻数量，默认大小为12篇
//     * @param offset 服务端与本地不同步所造成的偏移量
//     */
//    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0): Single<NewsPagination>
//
//    /**
//     * 获取新闻。
//     *
//     * @param page   页数，默认为第一页
//     * @param count  每页新闻数量，默认大小为12篇
//     * @param offset 服务端与本地不同步所造成的偏移量
//     * @param category 门户站点
//     */
//    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, category: String?, cachedPageNum: String? = null): Flowable<NewsPagination>
//
//    /**
//     * 从本地获取一个新闻页面
//     *
//     * @param page   页数，默认为第一页
//     * @param count  每页新闻数量，默认大小为12篇
//     * @param offset 服务端与本地不同步所造成的偏移量
//     * @param category 门户站点
//     */
//    fun getNewsPage(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, category: String?): Single<NewsPagination>
}