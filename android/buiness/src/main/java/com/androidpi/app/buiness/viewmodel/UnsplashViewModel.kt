package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.androidpi.app.base.vm.vo.Resource
import com.androidpi.common.networks.http.RetrofitClientFactory
import com.androidpi.data.remote.UnsplashApi
import com.androidpi.data.remote.dto.ResRandomPhotos
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by jastrelax on 2018/8/13.
 */
class UnsplashViewModel : ViewModel() {

    val randomPhotosResult = MutableLiveData<Resource<List<ResRandomPhotos>>>()

    private val api: UnsplashApi

    init {
        api = RetrofitClientFactory.newRetrofit(UnsplashApi.BASE_URL).create(UnsplashApi::class.java)
    }

    fun getRandomPhotos(count: Int) {
        api.randomPhotos(count = count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<ResRandomPhotos>> {
                    override fun onSuccess(t: List<ResRandomPhotos>) {
                        randomPhotosResult.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }
}
