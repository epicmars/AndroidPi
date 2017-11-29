package cn.androidpi.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE

/**
 * Created by jastrelax on 2017/11/2.
 */

@Dao
interface NewsDao {

    @Insert
    fun insertNews(vararg newsItems: News)

    @Query("select * from news order by publish_time desc limit :count offset :page * :count")
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE) : List<News>

    @Query("select * from news order by publish_time desc limit :count offset :page * :count + :offset")
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0) : List<News>
}
