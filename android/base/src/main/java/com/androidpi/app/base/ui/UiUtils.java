package com.androidpi.app.base.ui;

import android.content.Context;
import android.content.res.Resources;

import com.androidpi.app.pi.base.R;

public class UiUtils {

    public static int getStatusBarHeight(Context context) {
        final int statusBarHeight;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = resources.getDimensionPixelSize(R.dimen.app_status_bar_height);
        }
        return statusBarHeight;
    }
}