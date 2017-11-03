package cn.androidpi.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import cn.androidpi.data.local.NewsDatabase
import cn.androidpi.data.local.dao.NewsDao
import cn.androidpi.news.entity.News
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
        newsDao = newsDb!!.newsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        newsDb!!.close()
    }

    @Test
    fun testNewsDao() {

        var news = News()
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

        newsDao!!.insertNews(news)

        var newsPage = newsDao!!.getNews()
        assertEquals(1, newsPage.size)
    }

}
