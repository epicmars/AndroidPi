package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.app.viewmodel.vo.Resource
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.news.model.NewsModel
import cn.androidpi.news.model.NewsPage
import cn.androidpi.news.model.NewsPageModel
import dagger.Lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
class NewsViewModel @Inject constructor() : ViewModel(), NewsModel {

    @Inject
    lateinit var mNewsRepo: Lazy<NewsRepo>

    val mNews: MutableLiveData<Resource<NewsPage>> = MutableLiveData()

    var mPortal: String? = null

    var mNewsPageModel = NewsPageModel()

    init {
        mNews.value = Resource.loading(NewsPage())
        mNewsPageModel.lastCachedPageNum = "-1"
    }

    fun getLatestNews(isNext: Boolean, count: Int = NewsModel.PAGE_SIZE) {

        val page = if (isNext) mNewsPageModel.page else 0

        mNewsRepo.get().getLatestNews(page ?: 0, count, mNews.value?.data?.mOffset ?: 0, mPortal, mNewsPageModel.lastCachedPageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableSubscriber<NewsPageModel>() {

                    override fun onError(e: Throwable?) {
                        Timber.e(e)
                        mNews.value = Resource.error("加载新闻失败", null)
                    }

                    override fun onNext(t: NewsPageModel) {
                        mNewsPageModel = t
                        var newsPage = mNews.value?.data
                        if (newsPage == null) {
                            newsPage = NewsPage()
                        }
                        if (!isNext) {
                            newsPage.firstPage(t.newsList)
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
