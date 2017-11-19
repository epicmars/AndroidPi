package cn.androidpi.app.widget;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2017/11/17.
 */

public class PullDownHeaderBehavior extends HeaderBehavior implements PullDownRefresher{

    private List<PullDownListener> mListeners = new ArrayList<>();

    public PullDownHeaderBehavior() {

    }

    public PullDownHeaderBehavior(Context context) {
        super(context);

        addHeaderListener(new HeaderListener() {
            @Override
            public void onHide() {

            }

            @Override
            public void onShow() {

            }

            @Override
            public void onStopScroll() {
                for (PullDownListener l : mListeners) {
                    l.onRefresh();
                }
            }
        });
    }

    public void addPullDownListener(PullDownListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    public void removePullDownListener(PullDownListener listener) {
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
