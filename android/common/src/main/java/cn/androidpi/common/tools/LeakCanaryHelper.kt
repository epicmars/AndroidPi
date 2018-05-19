package cn.androidpi.common.tools

import android.app.Application
import cn.androidpi.common.BuildConfig
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

/**
 * Created by jastrelax on 2017/6/15.
 */

object LeakCanaryHelper {

    var refWatcher: RefWatcher? = null

    fun init(application: Application) {
        if (!BuildConfig.DEBUG)
            return
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(application)
    }
}
