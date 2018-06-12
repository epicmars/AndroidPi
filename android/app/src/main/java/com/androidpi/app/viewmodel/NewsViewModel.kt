package com.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.app.viewmodel.vo.Resource
import com.androidpi.news.repo.NewsRepo
import com.androidpi.news.model.NewsListModel
import com.androidpi.news.vo.NewsPage
import com.androidpi.news.vo.NewsPagination
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
class NewsViewModel @Inject constructor() : ViewModel(), NewsListModel {

    @Inject
    lateinit var mNewsRepo: Lazy<NewsRepo>

    val mNews: MutableLiveData<Resource<NewsPage>> = MutableLiveData()

    var mPortal: String? = null

    var mNewsPageModel = NewsPagination()

    init {
        mNews.value = Resource.loading(NewsPage())
        mNewsPageModel.lastCachedPageNum = "-1"
    }

    fun getLatestNews(isNext: Boolean, count: Int = NewsListModel.PAGE_SIZE) {
        Timber.d("isNext: $mPortal $isNext")

        val page = if (isNext) mNewsPageModel.nextPage else 0

        Timber.d("getLatestedNews $page")

        mNewsRepo.get().getLatestNews(page ?: 0, count, mNewsPageModel.offset, mPortal, mNewsPageModel.lastCachedPageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSubscriber<NewsPagination>() {

                    override fun onError(e: Throwable?) {
                        Timber.e(e)
                        mNews.value = Resource.error("加载新闻失败", null)
                    }

                    override fun onNext(t: NewsPagination) {
                        Timber.d("onNext $mPortal + ${t.page}")
                        mNewsPageModel = t
                        var newsPage = mNews.value?.data
                        if (newsPage == null) {
                            newsPage = NewsPage()
                        }
                        if (page == 0) {
                            newsPage.firstPage(t.newsList)
                            if (t.newsList.isEmpty()) {
                                mNews.value = Resource.error("加载新闻出错", newsPage)
                                return
                            }
                        } else {
                            newsPage.nextPage(t.newsList)
                        }
                        mNews.value = Resource.success(newsPage)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun refreshPage() {
        getLatestNews(false)
    }

    override fun nextPage() {
        getLatestNews(true)
    }
}
