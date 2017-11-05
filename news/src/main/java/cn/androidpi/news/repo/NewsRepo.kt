package cn.androidpi.news.repo

import cn.androidpi.news.entity.News
import io.reactivex.Single

/**
 * 仓储模型，定义最上层的业务接口。
 * Created by jastrelax on 2017/11/2.
 */

interface NewsRepo {

    companion object {
        // 新闻页面文章数默认大小
        const val PAGE_SIZE = 12
    }

    /**
     * 获取新闻
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE): Single<List<News>>
}