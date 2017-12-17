package cn.androidpi.data.local.dao

import android.arch.persistence.room.*
import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel.Companion.PAGE_SIZE
import io.reactivex.Single

/**
 * Created by jastrelax on 2017/11/2.
 */

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(vararg newsItems: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsList(newsList: List<News>)

    @Update
    fun updateNews(vararg newsItems: News)

    @Update
    fun updateNewsList(newsList: List<News>)

    @Query("select * from news where publish_time > :time order by publish_time asc limit 1")
    fun getNewsBefore(time: String): News?

    @Query("select * from news order by publish_time desc limit :count offset :page * :count")
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE) : List<News>

    @Query("select * from news order by publish_time desc limit :count offset :page * :count + :offset")
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0) : Single<List<News>>

    @Query("select * from news where portal = :portal or url like '%'||:portal||'%' order by publish_time desc limit :count offset :page * :count + :offset")
    fun getNews(page: Int = 0, count: Int = PAGE_SIZE, offset: Int = 0, portal: String) : Single<List<News>>

    @Query("select * from news where news_id = :newsId limit 1")
    fun findByNewsId(newsId: String): News?
}
