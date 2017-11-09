package cn.androidpi.app

import android.app.Activity
import android.app.Application
import cn.androidpi.app.di.DaggerHelper
import cn.androidpi.common.log.LogHelper
import cn.androidpi.common.tools.LeakCanaryHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
class AndroidPiApp : Application(), HasActivityInjector{

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerHelper.inject(this)
        LogHelper.init(this)
        LeakCanaryHelper.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}