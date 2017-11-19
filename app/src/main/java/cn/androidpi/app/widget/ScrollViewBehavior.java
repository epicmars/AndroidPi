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
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof HeaderBehavior
                    || behavior instanceof FooterBehavior;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        int offset = 0;
        if (behavior instanceof HeaderBehavior) {
            offset = dependency.getBottom() - child.getTop();
        } else if (behavior instanceof FooterBehavior) {
            offset = dependency.getTop() - child.getBottom();
        }
        ViewCompat.offsetTopAndBottom(child, offset);
        return false;
    }
}
