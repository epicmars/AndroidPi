package com.androidpi.app.cweather.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.androidpi.app.base.ui.BaseActivity
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.cweather.R
import com.androidpi.app.cweather.databinding.ActivityWeatherBinding

import kotlinx.android.synthetic.main.activity_weather.*

@BindLayout(R.layout.activity_weather)
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            var ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content, WeatherFragment.newInstance())
            ft.commit()
        }
    }

}
