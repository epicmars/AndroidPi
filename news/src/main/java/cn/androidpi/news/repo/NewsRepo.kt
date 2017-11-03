package cn.androidpi.news.repo

import cn.androidpi.news.entity.News
import io.reactivex.Single

/**
 * 仓储模型，定义最上层的业务接口。
 * Created by jastrelax on 2017/11/2.
 */

interface NewsRepo {

    /**
     * 获取新闻
     *
     * @param page   页数
     * @param count  每页新闻数量
     */
    fun getNews(page: Int, count: Int): Single<List<News>>
}