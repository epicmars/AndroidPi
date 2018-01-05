package cn.androidpi.news.model

/**
 * Created by jastrelax on 2018/1/5.
 */
interface BookmarkListModel {

    companion object {
        const val BOOKMARK_PAGE_SIZE = 20
    }

    fun refreshPage()

    fun nextPage()
}