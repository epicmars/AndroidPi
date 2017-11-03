package cn.androidpi.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/2.
 */

@Dao
interface NewsDao {

    @Insert
    fun insertNews(news: News)

    @Query("select * from news limit :count offset :page * :count ")
    fun getNews(page: Int = 0, count: Int = 10) : List<News>
}
