package cn.androidpi.data

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.androidpi.data.local.NEWS_MIGRATION_3_4
import cn.androidpi.data.local.NewsDatabase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by jastrelax on 2017/12/15.
 */
@RunWith(AndroidJUnit4::class)
class TestNewsMigration {

    val NEWS_TEST_DB = "news_test.db"

    @Rule
    var helper: MigrationTestHelper

    constructor() {
        helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                NewsDatabase::class.java.canonicalName,
                FrameworkSQLiteOpenHelperFactory())
    }

    @Test
    fun migrate3To4() {
        var db = helper.createDatabase(NEWS_TEST_DB, 3)

        val portals = arrayOf("ifeng.com", "qq.com", "163.com")

        for (portal in portals) {
            db.execSQL("insert into news (url) value 'http://tech.$portal/adaf1223'")
            db.execSQL("insert into news (url) value 'http://tech.$portal/adaf2adf'")
            db.execSQL("insert into news (url) value 'http://tech.$portal/adaf34'")
        }

        db.close()

        db = helper.runMigrationsAndValidate(NEWS_TEST_DB, 4, true, NEWS_MIGRATION_3_4)


        for (portal in portals) {
            val cursor = db.query("select * from news where url like '%$portal%'")
            while (cursor.moveToNext()) {
                val portalQueried = cursor.getString(cursor.getColumnIndex("portal"))
                assertEquals(portalQueried, portal)
            }
        }
        db.close()
    }

}