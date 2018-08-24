package com.androidpi.app.base.widget.literefresh;


/**
 * Created by jastrelax on 2018/8/24.
 */
public class HeaderRefreshStateMachine extends RefreshStateMachine {

    public HeaderRefreshStateMachine(ContentBehaviorController controller) {
        super(controller);
    }


    @Override
    protected void onStateChanged(int state) {
        switch (state) {
            case STATE_START:
                controller.onRefreshStart();
                break;
            case STATE_READY:
                controller.onReleaseToRefresh();
                break;
            case STATE_REFRESH:
                controller.onRefresh();
                break;
            case STATE_COMPLETE:
                controller.stopScroll(true);
                controller.onRefreshEnd();
                break;
            case STATE_IDEL:
            default:
                break;
        }
    }

    @Override
    public void refresh() {
        // To avoid unnecessary task enqueueing.
        if (currentState == STATE_REFRESH)
            return;
        controller.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (moveToState(STATE_REFRESH)) {
                    controller.showHeader();
                }
            }
        });
    }

    @Override
    public void refreshComplete() {
        refreshCompleted();
    }

    @Override
    public void refreshError(Exception exception) {
        refreshCompleted();
    }

    @Override
    public void load() {
        // ignore
    }

    @Override
    public void loadComplete() {
        // ignore
    }

    @Override
    public void loadError(Exception exception) {
        // ignore
    }

    protected void startRefreshing() {
        if (!controller.mRefreshListeners.isEmpty() && moveToState(STATE_REFRESH)) {
            controller.showHeader();
        } else {
            controller.reset();
        }
    }

    private void refreshCompleted() {
        controller.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moveToState(STATE_COMPLETE);
            }
        });
    }

    protected int transform(int currentOffset) {
        return currentOffset;
    }

    protected int readyRefreshOffset() {
        return controller.getBehavior().headerHeight;
    }

    @Override
    protected boolean isValidOffset(int currentOffset) {
        return currentOffset > 0;
    }
}
