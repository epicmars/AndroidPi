package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/22.
 */
class CoverNewsViewModel : ViewModel() {

    var mNewsList = ArrayList<News>()

    var mCoverNews  = MutableLiveData<List<News>>()

    init {
        mCoverNews.value = mNewsList
    }

    fun clear() {
        mNewsList.clear()
    }

    fun add(news: News) {
        mNewsList.add(news)
    }

    fun addAll(collection: Collection<News>) {
        mNewsList.addAll(collection)
    }

    fun getSize(): Int = mNewsList.size

    fun update() {
        mCoverNews.value = mNewsList
    }
}