package com.androidpi.app.base.widget.literefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by jastrelax on 2017/11/19.
 */

public class RefreshFooterBehavior<V extends View> extends FooterBehavior<V> implements Loader{

    public RefreshFooterBehavior(Context context) {
        super(context);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnPullingListener(OnPullListener listener) {
        controller.addOnPullingListener(listener);
    }

//    public void removeOnPullingListener(OnPullListener listener) {
//        if (null == listener)
//            return;
//        mPullListeners.remove(listener);
//    }

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
    }

    @Override
    public void load() {
        controller.load();
    }

    @Override
    public void loadComplete() {
        controller.loadComplete();
    }

    @Override
    public void loadError(Exception exception) {
        controller.loadError(exception);
    }

}
