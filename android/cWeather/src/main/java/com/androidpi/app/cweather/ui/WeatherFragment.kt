package com.androidpi.app.cweather.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.View

import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.base.widget.literefresh.OnRefreshListener
import com.androidpi.app.base.widget.literefresh.RefreshHeaderBehavior
import com.androidpi.app.cweather.R
import com.androidpi.app.cweather.data.remote.OpenWeatherMapApi
import com.androidpi.app.cweather.databinding.FragmentWeatherBinding
import com.androidpi.app.cweather.viewmodel.WeatherViewModel
import com.bumptech.glide.Glide

/**
 * Created by jastrelax on 2018/8/18.
 */
@BindLayout(value = R.layout.fragment_weather, injectable = false)
class WeatherFragment : BaseFragment<FragmentWeatherBinding>() {

    lateinit var viewModel: WeatherViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        val behavior = (binding.refreshHeader.layoutParams as CoordinatorLayout.LayoutParams).behavior as RefreshHeaderBehavior
        behavior.addOnRefreshListener(object : OnRefreshListener {
            override fun onRefreshComplete() {
            }

            override fun onRefreshReady() {
            }

            override fun onRefreshStart() {
            }

            override fun onRefresh() {
                viewModel.getCurrentWeather(30.67f, 104.08f)
            }
        })

        viewModel.weatherResult.observe(this, Observer { resCurrentWeatherResource ->
            if (resCurrentWeatherResource == null) return@Observer
            if (resCurrentWeatherResource.isSuccess) {
                behavior.refreshComplete()
                val weatherBean = resCurrentWeatherResource.data!!.weather[0]
                val mainBean = resCurrentWeatherResource.data!!.main
                binding.ivIcon.setImageResource(resources.getIdentifier("ic_" + weatherBean.icon, "drawable", context?.packageName))
                binding.tvDescription.text = weatherBean.description.capitalize()
                binding.tvTemperature.text = getString(R.string.format_temperature, mainBean.temp - 273.15)
            } else if (resCurrentWeatherResource.isError) {
                behavior.refreshComplete()
            }
        })

        behavior.refresh()
    }

    companion object {

        fun newInstance(): WeatherFragment {

            val args = Bundle()

            val fragment = WeatherFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
