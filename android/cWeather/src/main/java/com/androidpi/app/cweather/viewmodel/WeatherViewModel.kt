package com.androidpi.app.cweather.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.app.base.vm.vo.Resource

import com.androidpi.app.cweather.data.remote.OpenWeatherMapApi
import com.androidpi.app.cweather.data.remote.dto.ResCurrentWeather
import com.androidpi.common.networks.http.RetrofitClientFactory
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by jastrelax on 2018/8/18.
 */
class WeatherViewModel : ViewModel() {

    private var api: OpenWeatherMapApi
    val weatherResult = MutableLiveData<Resource<ResCurrentWeather>>()

    init {
        api = RetrofitClientFactory.newRetrofit(OpenWeatherMapApi.BASE_URL).create(OpenWeatherMapApi::class.java)
    }

    fun getCurrentWeather(lat: Float, lon: Float) {
        api.getCurrentWeather(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ResCurrentWeather> {
                    override fun onSuccess(t: ResCurrentWeather) {
                        weatherResult.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        weatherResult.value = Resource.error(e)
                    }
                })
    }

}
