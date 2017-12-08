package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.app.viewmodel.vo.Resource
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel
import cn.androidpi.news.model.NewsPage
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
class NewsViewModel @Inject constructor() : ViewModel(), NewsModel {

    @Inject
    lateinit var mNewsRepo: Lazy<NewsRepo>

    val mNews: MutableLiveData<Resource<NewsPage>> = MutableLiveData()

    init {
        mNews.value = Resource.loading(NewsPage())
    }

    fun getLatestNews(isNext: Boolean, count: Int = NewsModel.PAGE_SIZE) {

        val page = if (isNext) mNews.value?.data?.getNextPageNum() else 0

        mNewsRepo.get().getLatestNews(page ?: 0, count, mNews.value?.data?.mOffset ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<News>> {
                    override fun onError(e: Throwable?) {
                        Timber.e(e)
                        mNews.value = Resource.error("加载新闻失败", null)
                    }

                    override fun onSuccess(t: List<News>) {
                        if (!isNext) {
                            mNews.value?.data?.firstPage(t)
                        } else {
                            mNews.value?.data?.nextPage(t)
                        }
                        mNews.value = Resource.success(mNews.value!!.data!!)
                    }

                    override fun onSubscribe(d: Disposable?) {
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
