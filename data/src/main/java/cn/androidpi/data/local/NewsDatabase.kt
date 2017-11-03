package cn.androidpi.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/2.
 */

@Database(entities = arrayOf(News::class), version = 1)
@TypeConverters(DateConverter::class, StringArrayConverter::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

}