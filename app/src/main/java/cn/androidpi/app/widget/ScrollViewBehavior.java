package cn.androidpi.app.widget;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by jastrelax on 2017/11/16.
 */

public class ScrollViewBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp && lp.getBehavior() != null) {
            return lp.getBehavior() instanceof HeaderBehavior;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        if (behavior instanceof HeaderBehavior) {
            int verticalOffset = dependency.getBottom() - child.getTop();
            ViewCompat.offsetTopAndBottom(child, verticalOffset);
        }
        return false;
    }
}
