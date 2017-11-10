package cn.androidpi.news.model

/**
 * Created by jastrelax on 2017/11/7.
 */
interface NewsModel {

    companion object {
        // 新闻页面文章数默认大小
        const val PAGE_SIZE = 12
    }

    fun refreshPage()

    fun nextPage()

}