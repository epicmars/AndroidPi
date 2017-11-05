package cn.androidpi.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.androidpi.data.local.NewsDatabase
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.news.entity.News
import cn.androidpi.news.repo.NewsRepo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by jastrelax on 2017/11/2.
 */

@RunWith(AndroidJUnit4::class)
class TestNewsDao {

    var newsDao: NewsDao? = null
    var newsDb : NewsDatabase? = null

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

    @Test
    @Throws(Exception::class)
    fun testNewsDao() {

        insertNews(1)

        var newsPage = newsDao!!.getNews()
        assertEquals(1, newsPage.size)
        println(newsPage.toTypedArray())

        insertNews(100)
        var firstPage = newsDao!!.getNews(0)
        assertEquals(NewsRepo.PAGE_SIZE, firstPage.size)

        println(firstPage.toTypedArray())

        var secondPage = newsDao!!.getNews(1)
        assertEquals(NewsRepo.PAGE_SIZE, secondPage.size)

        println(secondPage.toTypedArray())

    }

    fun insertNews(count: Int) {

        val newsItems = Array<News>(count, {
            i ->
            val news = News()
            news.category = "news"
            news.keywords = arrayOf("nasa",
                    "哈勃望远镜",
                    "哈勃",
                    "大气层")
            news.publishTime = "2017-11-02 13:52:34"
            news.originTitle = "原标题：哈勃望远镜， 无法与你说再见"
            news.sourceUrl = "http://www.stdaily.com/"
            news.sourceName = "科技日报社-中国科技网"
            news.title = "哈勃望远镜慢慢坠入大气层，NASA现在也无能为力"
            news.url = "http://tech.163.com/17/1102/13/D288VV5200097U81.html"

            news
        })

        newsDao!!.insertNews(*newsItems)
    }

}
