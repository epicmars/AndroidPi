package com.androidpi.common.log

import android.app.Application

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

import timber.log.Timber

/**
 * Created by jastrelax on 2017/6/13.
 */

object LogHelper {

    fun init(application: Application) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Timber.plant(Timber.DebugTree())
    }
}
