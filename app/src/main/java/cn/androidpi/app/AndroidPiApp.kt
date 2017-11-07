package cn.androidpi.app

import android.app.Application
import cn.androidpi.common.log.LogHelper
import cn.androidpi.common.tools.LeakCanaryHelper

/**
 * Created by jastrelax on 2017/11/7.
 */
class AndroidPiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        LogHelper.init(this)
        LeakCanaryHelper.init(this)
    }
}