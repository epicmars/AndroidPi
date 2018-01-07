package cn.androidpi.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import cn.androidpi.news.entity.Bookmark

/**
 * Created by jastrelax on 2018/1/5.
 */
@Dao
interface BookmarkDao {

    @Insert
    fun insertOne(bookmark: Bookmark): Long

    @Update
    fun update(vararg bookmarks: Bookmark)

    @Query("select * from bookmark where url = :url limit 1")
    fun findByUrl(url: String?): Bookmark?

    @Query("select * from bookmark order by timestamp desc limit :size")
    fun firstPage(size: Int): List<Bookmark>?

    @Query("select * from bookmark order by timestamp desc limit :size offset :page * :size")
    fun nextPage(page: Int, size: Int): List<Bookmark>?
}