package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.data.repository.NewsRepo
import cn.androidpi.news.entity.News
import cn.androidpi.news.model.NewsModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
class NewsViewModel @Inject constructor() : ViewModel(), NewsModel {

    @Inject
    var mNewsRepo: NewsRepo? = null

    @Inject
    var mNews: MutableLiveData<List<News>>? = null

    override fun getLatestNews(page: Int, count: Int) {

        mNewsRepo?.getLatestNews(page, count)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : SingleObserver<List<News>> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onSuccess(t: List<News>?) {
                        mNews?.value = t
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }
                })
    }
}