package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2018/8/24.
 */
public class FooterBehaviorController extends BehaviorController<FooterBehavior> {

    public FooterBehaviorController(FooterBehavior behavior) {
        super(behavior);
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
        if (delegate != null) {

            super.load();
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    load();
                }
            });
        }
    }

    @Override
    public void loadComplete() {
        if (delegate != null) {
            super.loadComplete();
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    loadComplete();
                }
            });
        }
    }

    @Override
    public void loadError(Exception exception) {
        if (delegate != null) {
            super.loadError(exception);
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    loadError(exception);
                }
            });
        }
    }
}
