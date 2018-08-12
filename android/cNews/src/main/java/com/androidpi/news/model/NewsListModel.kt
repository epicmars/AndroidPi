package com.androidpi.news.model

import com.androidpi.news.R

/**
 * Created by jastrelax on 2017/11/7.
 */
interface NewsListModel {

    companion object {
        // 新闻页面文章数默认大小
        const val PAGE_SIZE = 12

        val categories = mapOf(
                Pair("general", R.string.category_general)
//                Pair("sports", R.string.category_sports),
//                Pair("science", R.string.category_science),
//                Pair("technology", R.string.category_technology),
//                Pair("business", R.string.category_business),
//                Pair("entertainment", R.string.category_entertainment),
//                Pair("health", R.string.category_health)
        )
    }

    fun refreshPage()

    fun nextPage()

}