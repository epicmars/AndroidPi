---
title: LiteRefresh v0.4
date: 2018-08-24 19:07:17 +0800
---
## 模式设计
目前的做法使用继承来完成滑动的暴露和刷新状态的转换，然后Behavior子类，实现并设置改监听器，然后再根据监听器传入的值向外暴露出滑动距离，并根据一个状态机的转换规则暴露刷新状态，同时向外暴露一个Refresher作为状态的控制器。

                                        ViewOffsetBehavior
                                        AnimationOffsetBehavior
                                                   VerticalBoundaryBehavior
        ContentBehavior                      HeaderBehavior            FooterBehavior
    RefreshContentBehavior              RefreshHeaderBehavior      RefreshFooterBehavior

目前RefreshContentBehavior，RefreshHeaderBehavior，RefreshFooterBehavior主要作用是暴露滑动状态和刷新状态，以及作为控制器。这说明其职责实际上可以使用桥接的方式抽象到另一个层级，即和它们的父类一个层级。这样可以在运行时改变其暴露出的行为，这样还可以将部分原来写死在Behavior中与滑动相关性不大的逻辑也让其负责，如行为模式的配置与变化。

## Behavior的状态设计
目前采用如下接口监听原始的滑动事件，其中接口传递的是经过坐标系变换后的偏移量。需要注意onStopScroll仅表示滑动触摸操作的结束，可能对应TouchEvent.UP事件，但不表明滑动的结束，随后可能使用延时的动画来更新视图：
```java
    public interface ScrollListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);
    }
```

目前ScrollerListener接口暴露出的滑动是经过坐标系转换的，也就是可以对Header和Footer采用一致的状态转换设计。

根据监听到的滑动事件进行状态转移的设计，目前一个完整的刷新状态集合如下：
```java
    private static final int STATE_IDEL = 0;        // 初始状态
    private static final int STATE_START = 1;       // 开始刷新，发生触摸事件
    private static final int STATE_READY = 2;       // 达到刷新触发条件
    private static final int STATE_REFRESH = 3;     // 正在刷新
    private static final int STATE_COMPLETE = 4;    // 刷新结束
```