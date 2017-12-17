package cn.androidpi.data.repository

import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE
import cn.androidpi.news.model.NewsPageModel
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
    fun refreshNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>

    /**
     * 获取最新的新闻，首先从服务端更新并保存到本地后，在从本地取出。如果从服务端获取失败了，
     * 直接从本地取出。
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>

    /**
     * 获取新闻，直接从本地取出已保存的新闻。
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0): Single<List<News>>

    /**
     * 获取新闻，直接从本地取出已保存的新闻。
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, portal: String): Single<List<News>>

    /**
     * 获取新闻。
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     * @param offset 服务端与本地不同步所造成的偏移量
     */
    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0): Single<NewsPageModel>

    /**
     * 获取新闻。
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     * @param offset 服务端与本地不同步所造成的偏移量
     * @param portal 门户站点
     */
    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, portal: String?, cachedPageNum: String? = null): Flowable<NewsPageModel>

    /**
     * 从本地获取一个新闻页面
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     * @param offset 服务端与本地不同步所造成的偏移量
     * @param portal 门户站点
     */
    fun getNewsPage(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, portal: String?): Single<NewsPageModel>
}