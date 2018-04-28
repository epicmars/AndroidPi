package cn.androidpi.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.androidpi.news.local.dao.BookmarkDao
import cn.androidpi.news.entity.Bookmark
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by jastrelax on 2018/1/6.
 */
@RunWith(AndroidJUnit4::class)
class TestBookmarkDao {

    var mDao: BookmarkDao? = null
    var mDb: NewsDatabase? = null

    @Before
    fun setup(){
        val context = InstrumentationRegistry.getTargetContext()
        mDb = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        mDao = mDb?.bookmarkDao()
    }

    @After
    fun exit() {
        mDb?.close()
    }

    @Test
    fun testInsertAndUpdate() {

        val bm = Bookmark()
        bm.url = "http://tech.163.com/17/1102/13/D288VV5200097U81.html"
        bm.html = "html"

        // insertion
        mDao?.insert(bm)
        val bmInserted = mDao?.findByUrl(bm.url)
        assertEquals(bm.html, bmInserted?.html)

        // update
        bmInserted?.articleHtml = "articleHtml"
        // will fail if bmInserted is null
        mDao?.update(bmInserted!!)

        val bmUpdated = mDao?.findByUrl(bmInserted?.url)
        assertEquals(bmInserted!!.articleHtml, bmUpdated!!.articleHtml)
    }


    @Test
    fun testPagination() {

        insertBookmarks(20)

        val bookmarks =  mDao?.firstPage(12)

        assertEquals(bookmarks?.size, 12)

        var next = mDao?.nextPage(0, 12)
        assertEquals(next?.size, 12)

        next = mDao?.nextPage(1, 12)
        assertEquals(next?.size, 8)

        next = mDao?.nextPage(2, 12)
        assertEquals(next?.size, 0)

    }

    @Test
    fun testInsertNull() {
        mDao?.insert(null)
    }

    fun insertBookmarks(count: Int) {
        for(i in 0 until count) {
            val bm = Bookmark()
            bm.url = "http://tech.163.com/17/1102/13/D288VV5200097U81${i}.html"
            bm.html = "html"
            bm.articleHtml = "articleHtml"
            mDao?.insert(bm)
        }
    }

}