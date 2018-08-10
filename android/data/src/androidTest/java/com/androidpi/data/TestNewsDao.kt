package com.androidpi.data

import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.androidpi.news.local.dao.NewsDao
import com.androidpi.news.entity.News
import com.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jastrelax on 2017/11/2.
 */

@RunWith(AndroidJUnit4::class)
class TestNewsDao {

    var newsDao: NewsDao? = null
    var newsDb : NewsDatabase? = null
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val now: Calendar = Calendar.getInstance()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        newsDb = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
                .allowMainThreadQueries().build()
//        newsDb = Room.databaseBuilder(context, NewsDatabase::class.java, "test_news.db")
//                .build()
        newsDao = newsDb!!.newsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        newsDb!!.close()
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun testInsertingNews() {
        val news = createOneNews()
        news.id = null
        news.newsId = "afbadc01239453920"
        newsDao!!.insert(news, news)
    }

    @Test
    @Throws(Exception::class)
    fun testGetNews() {

        insertNews(100)
        var firstPage = newsDao!!.getNews(0)
        // page size is [PAGE_SIZE]
        assertEquals(PAGE_SIZE, firstPage.size)
        // order by publish time in descending order
        val it = firstPage.iterator()
        while (it.hasNext()) {
            val current = it.next()
            val next = it.next()
            if (next != null) {
                assertNotNull(next.images)
                assertTrue(current.publishTime!! >= next.publishTime!!)
            }
        }

        println(firstPage)

        var secondPage = newsDao!!.getNews(1)
        assertEquals(PAGE_SIZE, secondPage.size)

        println(secondPage)
    }

    fun insertNews(count: Int) {

        val newsItems = Array<News>(count, {
            i ->
            createOneNews()
        })

        newsDao!!.insert(*newsItems)
    }

    fun createOneNews(): News {
        val news = News()
        news.category = "news"
        news.keywords = arrayOf("nasa",
                "哈勃望远镜",
                "哈勃",
                "大气层")
        now.add(Calendar.SECOND, 10)
        news.publishTime = dateFormat.format(now.time)
        news.originTitle = "原标题：哈勃望远镜， 无法与你说再见"
        news.sourceUrl = "http://www.stdaily.com/"
        news.sourceName = "科技日报社-中国科技网"
        news.title = "哈勃望远镜慢慢坠入大气层，NASA现在也无能为力"
        news.url = "http://tech.163.com/17/1102/13/D288VV5200097U81.html"
        news.images = arrayOf("dsadf")

        return news
    }

}
