package cn.androidpi.common.tools

import android.app.Application

import com.squareup.leakcanary.LeakCanary

/**
 * Created by jastrelax on 2017/6/15.
 */

object LeakCanaryHelper {

    fun init(application: Application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(application)
    }
}
