package cn.androidpi.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/19.
 */

public class PullUpFooterBehavior<V extends View> extends FooterBehavior<V> implements PullingRefresher{

    private List<PullingListener> mListeners = new ArrayList<>();

    public PullUpFooterBehavior() {
        this(null, null);
    }

    public PullUpFooterBehavior(Context context) {
        this(context, null);
    }

    public PullUpFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        addFooterListener(new FooterListener() {
            @Override
            public void onHide() {

            }

            @Override
            public void onShow() {

            }

            @Override
            public void onStopScroll() {
                for (PullingListener l : mListeners) {
                    l.onRefresh();
                }
            }
        });
    }

    public void addPullUpListener(PullingListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removePullUpListener(PullingListener listener) {
        if (null == listener)
            return;
        mListeners.remove(listener);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void refreshTimeout() {

    }

    @Override
    public void refreshCancelled() {

    }

    @Override
    public void refreshException(Exception exception) {

    }
}
