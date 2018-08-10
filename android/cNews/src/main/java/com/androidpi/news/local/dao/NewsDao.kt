package com.androidpi.news.local.dao

import android.arch.persistence.room.*
import com.androidpi.news.entity.News
import com.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import io.reactivex.Single

/**
 * Created by jastrelax on 2017/11/2.
 */

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg newsItems: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsList: List<News>)

    @Update
    fun updateNews(vararg newsItems: News)

    @Update
    fun updateNewsList(newsList: List<News>)

//    @Query("select * from news where publish_time > :time order by publish_time asc limit 1")
//    fun getNewsBefore(time: String): News?
//
//    @Query("select * from news order by publish_time desc limit :count offset :page * :count")
//    fun getNews(page: Int = 0, count: Int = PAGE_SIZE) : List<News>
//
//    @Query("select * from news order by publish_time desc limit :count offset :page * :count + :offset")
//    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0) : Single<List<News>>
//
//    @Query("select * from news where portal = :portal or url like '%'||:portal||'%' order by publish_time desc limit :count offset :page * :count + :offset")
//    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, portal: String) : Single<List<News>>

    @Query("select * from news where url = :url limit 1")
    fun findByUrl(url: String): News?
}
