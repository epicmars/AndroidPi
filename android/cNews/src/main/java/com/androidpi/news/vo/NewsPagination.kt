package com.androidpi.news.vo

import com.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/12/16.
 */
class NewsPagination() {

    companion object {
        val FIRST_PAGE = 1
    }

    var newsList: MutableList<News> = mutableListOf()

    var page: Int = 1

    constructor(page: Int, list: List<News>): this() {
        this.page = page
        this.newsList.addAll(list)
    }

    fun nextPage(isNext: Boolean) : Int {
        page = if (isNext) page + 1 else 1
        return  page
    }

    fun isFirstPage() : Boolean {
        return page == FIRST_PAGE
    }
}