/*
 * Copyright 2018 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidpi.app.base.widget.literefresh;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Super class of header and footer behavior controller.
 */
public abstract class VerticalIndicatorBehaviorController<B extends VerticalIndicatorBehavior>
        extends BehaviorController<B> {

    /**
     * Follow content view.
     */
    public static final int MODE_FOLLOW = 0;
    /**
     * Still, does not follow content view.
     */
    public static final int MODE_STILL = 1;

    /**
     * Follow when scroll down.
     */
    public static final int MODE_FOLLOW_DOWN = 2;

    /**
     * Follow when scroll up.
     */
    public static final int MODE_FOLLOW_UP = 3;

    protected int mode = MODE_FOLLOW;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public VerticalIndicatorBehaviorController(B behavior) {
        super(behavior);
    }

    public abstract int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent,
                                                                 View child,
                                                                 View dependency,
                                                                 VerticalIndicatorBehavior behavior,
                                                                 ScrollingContentBehavior contentBehavior);

    public abstract float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                              VerticalIndicatorBehavior behavior,
                                                              ScrollingContentBehavior contentBehavior,
                                                              int currentOffset, int offsetDelta);

    public abstract int transformOffsetCoordinate(CoordinatorLayout parent, View child,
                                                  VerticalIndicatorBehavior behavior,
                                                  int currentOffset);

    /**
     * Tell if the hidden part of the view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * in that case it's considered to be invisible.
     *
     * @return true if hidden part of view is visible,
     * otherwise return false.
     */
    public abstract boolean isHiddenPartVisible(CoordinatorLayout parent, View child,
                                                VerticalIndicatorBehavior behavior);
}
