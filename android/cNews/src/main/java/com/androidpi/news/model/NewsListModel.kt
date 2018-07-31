package com.androidpi.news.model

/**
 * Created by jastrelax on 2017/11/7.
 */
interface NewsListModel {

    companion object {
        // 新闻页面文章数默认大小
        const val PAGE_SIZE = 12
        const val portal_all = "all"
        const val portal_163 = "163.com"
        const val portal_qq = "qq.com"
        const val portal_ifeng = "ifeng.com"

        const val portal_all_name = "首页"
        const val portal_qq_name = "腾讯"
        const val portal_163_name = "网易"
        const val portal_ifeng_name = "凤凰"

    }

    fun refreshPage()

    fun nextPage()

}