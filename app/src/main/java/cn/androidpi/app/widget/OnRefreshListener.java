package cn.androidpi.app.widget;

/**
 * Created by jastrelax on 2017/11/29.
 */

public interface OnRefreshListener {

    void onRefreshStart();

    void onRefreshReady();

    void onRefresh();

    void onRefreshComplete();
}
