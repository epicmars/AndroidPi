package cn.androidpi.common.web;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.webkit.WebView;

import cn.androidpi.common.BuildConfig;

/**
 * Created by jastrelax on 2018/1/3.
 */

public class WebViewHelper {

    public static void configDebugSettings(Context context) {
        if (!BuildConfig.DEBUG) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }
}
