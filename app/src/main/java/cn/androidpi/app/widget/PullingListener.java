package cn.androidpi.app.widget;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface PullingListener {

    void onStartPulling(int max);

    void onPulling(int current, int delta, int max);

    void onStopPulling(int current, int max);

    void onRefreshStart();

    void onRefreshReady();

    void onRefresh();

    void onRefreshComplete();
}
