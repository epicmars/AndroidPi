package cn.androidpi.app.widget;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface PullDownListener {

    void onRefresh();

    void onRefreshFinish();

    void onRefreshTimeout();

    void onRefreshCancelled();

    void onRefreshException(Exception exception);
}
