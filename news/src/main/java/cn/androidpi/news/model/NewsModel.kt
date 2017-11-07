package cn.androidpi.news.model

/**
 * Created by jastrelax on 2017/11/7.
 */
interface NewsModel {

    companion object {
        // 新闻页面文章数默认大小
        const val PAGE_SIZE = 12
    }

    /**
     * 获取最新的新闻
     *
     * @param page   页数，默认为第一页
     * @param count  每页新闻数量，默认大小为12篇
     */
    fun getLatestNews(page: Int = 0, count: Int = PAGE_SIZE)
}