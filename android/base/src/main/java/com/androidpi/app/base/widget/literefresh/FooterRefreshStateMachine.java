package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class FooterRefreshStateMachine extends RefreshStateMachine{

    public FooterRefreshStateMachine(ContentBehaviorController controller) {
        super(controller);
    }

    @Override
    protected void onStateChanged(int state) {
        switch (state) {
            case STATE_START:
                controller.onLoadStart();
                break;
            case STATE_READY:
                controller.onReleaseToLoad();
                break;
            case STATE_REFRESH:
                controller.onLoad();
                break;
            case STATE_COMPLETE:
                controller.stopScroll(true);
                controller.onLoadEnd();
                break;
            case STATE_IDEL:
            default:
                break;
        }
    }

    @Override
    public void refresh() {
        // ignore
    }

    @Override
    public void refreshComplete() {
        // ignore
    }

    @Override
    public void refreshError(Exception exception) {
        // ignore
    }

    @Override
    public void load() {
        // To avoid unnecessary task enqueueing.
        if (currentState == STATE_REFRESH)
            return;
        controller.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (moveToState(STATE_REFRESH)) {
                    controller.showFooter();
                }
            }
        });
    }

    @Override
    public void loadComplete() {
        loadCompleted();
    }

    @Override
    public void loadError(Exception exception) {
        loadCompleted();
    }


    @Override
    protected int transform(int currentOffset) {
        return - (currentOffset + controller.getBehavior().getChild().getHeight()) + controller.getBehavior().getParent().getHeight();
    }

    @Override
    protected int readyRefreshOffset() {
        return controller.getBehavior().getFooterHeight();
    }

    @Override
    protected void startRefreshing() {
        if (!controller.mLoadListeners.isEmpty() && moveToState(STATE_REFRESH)) {
            controller.showFooter();
        } else {
            controller.reset();
        }
    }

    private void loadCompleted() {
        controller.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moveToState(STATE_COMPLETE);
            }
        });
    }

    @Override
    protected boolean isValidOffset(int currentOffset) {
        return (currentOffset + controller.getBehavior().getChild().getHeight()) < controller.getBehavior().getParent().getHeight();
    }
}
