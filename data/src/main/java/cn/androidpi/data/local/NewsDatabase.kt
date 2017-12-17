package cn.androidpi.data.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/2.
 */

@Database(entities = arrayOf(News::class), version = 4)
@TypeConverters(DateConverter::class, StringArrayConverter::class)
abstract class NewsDatabase : RoomDatabase() {

    companion object {
        val DATABASE_NAME = "news.db"
    }

    abstract fun newsDao(): NewsDao

}

object NEWS_MIGRATION_1_2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE news ADD COLUMN images TEXT")
    }
}

object NEWS_MIGRATION_2_3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE news ADD COLUMN context TEXT")
    }
}

object NEWS_MIGRATION_3_4 : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE news ADD COLUMN portal TEXT")
        database.execSQL("UPDATE news SET portal = '163.com' WHERE url LIKE '%163.com%';")
        database.execSQL("UPDATE news SET portal = 'ifeng.com' WHERE url LIKE '%ifeng.com%';")
        database.execSQL("UPDATE news SET portal = 'qq.com' WHERE url LIKE '%qq.com%';")
    }
}