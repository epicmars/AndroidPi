package cn.androidpi.app

import android.app.Activity
import android.support.multidex.MultiDexApplication
import cn.androidpi.app.di.DaggerHelper
import cn.androidpi.common.log.LogHelper
import cn.androidpi.common.tools.LeakCanaryHelper
import cn.androidpi.common.web.WebViewHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
class AndroidPiApp : MultiDexApplication(), HasActivityInjector{

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerHelper.inject(this)
        LogHelper.init(this)
        LeakCanaryHelper.init(this)
        WebViewHelper.configDebugSettings(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}