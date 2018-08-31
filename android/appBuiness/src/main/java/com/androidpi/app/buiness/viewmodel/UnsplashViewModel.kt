package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.androidpi.app.base.vm.vo.Resource
import com.androidpi.app.buiness.vo.UnsplashPhotoPage
import com.androidpi.common.networks.http.RetrofitClientFactory
import com.androidpi.data.remote.UnsplashApi
import com.androidpi.data.remote.dto.ResUnsplashPhoto
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by jastrelax on 2018/8/13.
 */
class UnsplashViewModel : ViewModel() {

    val randomPhotosResult = MutableLiveData<Resource<UnsplashPhotoPage>>()

    private val api: UnsplashApi

    var page: Int = UnsplashPhotoPage.FIRST_PAGE

    init {
        api = RetrofitClientFactory.newRetrofit(UnsplashApi.BASE_URL).create(UnsplashApi::class.java)
    }

    fun getRandomPhotos(count: Int = 10, page: Int = 0) {
        this.page = page
        api.randomPhotos(count = count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<ResUnsplashPhoto>> {
                    override fun onSuccess(t: List<ResUnsplashPhoto>) {
                        randomPhotosResult.value = Resource.success(UnsplashPhotoPage(page, t))
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        randomPhotosResult.value = Resource.error(UnsplashPhotoPage(page, null))
                    }
                })
    }

    fun firstPage() {
        getRandomPhotos(page = UnsplashPhotoPage.FIRST_PAGE)
    }

    fun nextPage() {
        getRandomPhotos(page = page + 1)
    }
}
