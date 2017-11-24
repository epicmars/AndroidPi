package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.news.entity.News

/**
 * Created by jastrelax on 2017/11/22.
 */
class CoverNewsViewModel : ViewModel() {

    var mCoverNews  = MutableLiveData<MutableList<News>>()

    init {
        mCoverNews.value = ArrayList<News>()
    }

    fun clear() {
        mCoverNews.value?.clear()
    }

    fun add(news: News) {
        mCoverNews.value?.add(news)
    }

    fun addAll(collection: Collection<News>) {
        mCoverNews.value?.addAll(collection)
    }

    fun getSize(): Int = mCoverNews.value!!.size

    fun update() {
        mCoverNews.value = mCoverNews.value
    }
}