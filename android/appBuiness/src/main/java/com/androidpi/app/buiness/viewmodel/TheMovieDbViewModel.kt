package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.androidpi.app.base.vm.vo.Resource
import com.androidpi.app.buiness.utils.RxUtils
import com.androidpi.common.datetime.DateTimeUtils
import com.androidpi.common.networks.http.RetrofitClientFactory
import com.androidpi.data.remote.TheMovieDbApi
import com.androidpi.data.remote.dto.ResMoviePage
import com.androidpi.data.remote.dto.ResTrendingPage
import com.androidpi.data.remote.dto.ResTvPage
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import java.util.Calendar
import java.util.Date

/**
 * Created by jastrelax on 2018/9/7.
 */
class TheMovieDbViewModel : ViewModel() {

    companion object {
        const val TRENDING_ALL = "all"
        const val TRENDING_MOVIE = "movie"
        const val TRENDING_TV = "tv"

        const val TRENDING_TIMEWINDOW_WEEK = "week"
        const val TRENDING_TIMEWINDOW_DAY = "day"
    }

    internal var api = RetrofitClientFactory
            .newRetrofit(TheMovieDbApi.BASE_URL_V3)
            .create(TheMovieDbApi::class.java)

    val movieWithinMonthResults = MutableLiveData<Resource<ResMoviePage>>()

    val tvWithinMonthResults = MutableLiveData<Resource<ResTvPage>>()

    val weekTrendingAllResults = MutableLiveData<Resource<ResTrendingPage>>()

    val weekTrendingMovieResults = MutableLiveData<Resource<ResTrendingPage>>()

    val weekTrendingTvResults = MutableLiveData<Resource<ResTrendingPage>>()

    val dayTrendingAllResults = MutableLiveData<Resource<ResTrendingPage>>()

    fun getMovieWithinMonth() {
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start = calendar.time

        api.discoverMovie(DateTimeUtils.FORMAT_DATE.format(start),
                DateTimeUtils.FORMAT_DATE.format(now))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ResMoviePage> {
                    override fun onSuccess(t: ResMoviePage) {
                        movieWithinMonthResults.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        movieWithinMonthResults.value = Resource.loading();
                    }

                    override fun onError(e: Throwable) {
                        movieWithinMonthResults.value = Resource.error(e)
                    }
                })
    }

    fun getTvWithinMonth() {
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start = calendar.time

        api.discoverTv(DateTimeUtils.FORMAT_DATE.format(start),
                DateTimeUtils.FORMAT_DATE.format(now))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ResTvPage> {
                    override fun onSuccess(t: ResTvPage) {
                        tvWithinMonthResults.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        tvWithinMonthResults.value = Resource.loading()
                    }

                    override fun onError(e: Throwable) {
                        tvWithinMonthResults.value = Resource.error(e)
                    }
                })
    }

    fun getWeekTrending(type: String, data: MutableLiveData<Resource<ResTrendingPage>>) {
        getTrending(type, TRENDING_TIMEWINDOW_WEEK, data)
    }

    fun getDayTrending(type: String, data: MutableLiveData<Resource<ResTrendingPage>>) {
        getTrending(type, TRENDING_TIMEWINDOW_DAY, data)
    }

    fun getTrending(type: String, timeWindow: String, data: MutableLiveData<Resource<ResTrendingPage>>) {
        api.trending(type, timeWindow)
                .compose(RxUtils.networkIO())
                .subscribe(object : SingleObserver<ResTrendingPage> {
                    override fun onSuccess(t: ResTrendingPage) {
                        data.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        data.value = Resource.loading()
                    }

                    override fun onError(e: Throwable) {
                        data.value = Resource.error(e)
                    }
                })
    }
}
