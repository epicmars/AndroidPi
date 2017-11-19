package cn.androidpi.app.widget;

/**
 * Created by jastrelax on 2017/11/16.
 */

public interface PullingRefresher {

    void refresh();

    void refreshFinish();

    void refreshTimeout();

    void refreshCancelled();

    void refreshException(Exception exception);
}
