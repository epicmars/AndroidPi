package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class HeaderBehaviorController extends BehaviorController<HeaderBehavior> {

    public HeaderBehaviorController(HeaderBehavior behavior) {
        super(behavior);
    }

    @Override
    public void refresh() {
        // Make sure delegate controller has been set, otherwise the refresh state machine will not work.
        if (delegate != null) {
            super.refresh();
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        }
    }

    @Override
    public void refreshComplete() {
        if (delegate != null) {
            super.refreshComplete();
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    refreshComplete();
                }
            });
        }
    }

    @Override
    public void refreshError(Exception exception) {
        if (delegate != null) {
            super.refreshError(exception);
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    refreshError(exception);
                }
            });
        }
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
}
