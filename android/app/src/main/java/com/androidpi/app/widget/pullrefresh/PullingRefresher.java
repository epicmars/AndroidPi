package com.androidpi.app.widget.pullrefresh;

/**
 * An controller of pulling behaviors.
 *
 * <p>
 *     If using in multiple threads, the implementations should be thread safe.
 * </p>
 * Created by jastrelax on 2017/11/16.
 */
public interface PullingRefresher {

    void refresh();

    void refreshComplete();

    void refreshTimeout();

    void refreshCancelled();

    void refreshException(Exception exception);
}
