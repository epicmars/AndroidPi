package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.app.buiness.viewmodel.vo.Resource
import com.androidpi.news.entity.News
import com.androidpi.news.repo.NewsRepo
import com.androidpi.news.model.NewsListModel
import com.androidpi.news.vo.NewsPagination
import dagger.Lazy
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
class NewsViewModel @Inject constructor() : ViewModel(), NewsListModel {

    @Inject
    lateinit var mNewsRepo: Lazy<NewsRepo>

    val mNews: MutableLiveData<Resource<List<News>>> = MutableLiveData()

    var mCategory: String? = null

    var mNewsPageModel = NewsPagination()

    init {
        mNewsPageModel.lastCachedPageNum = "-1"
    }

    fun getLatestNews(isNext: Boolean, count: Int = NewsListModel.PAGE_SIZE) {
        Timber.d("isNext: $mCategory $isNext")

        val page = if (isNext) mNewsPageModel.nextPage else 0

        Timber.d("getLatestedNews $page")

        mNewsRepo.get().refreshNews(page ?: 0, count, mCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<News>> {

                    override fun onSuccess(t: List<News>) {
                        mNews.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        mNews.value = Resource.loading()
                    }

                    override fun onError(e: Throwable) {

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
