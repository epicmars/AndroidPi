---
title: LiteRefresh v0.4
date: 2018-08-24 19:07:17 +0800
---
## 模式设计
目前的做法使用继承来完成滑动的暴露和刷新状态的转换，然后Behavior子类，实现并设置改监听器，然后再根据监听器传入的值向外暴露出滑动距离，并根据一个状态机的转换规则暴露刷新状态，同时向外暴露一个Refresher作为状态的控制器。

                                   ViewOffsetBehavior
                                AnimationOffsetBehavior
                                                VerticalBoundaryBehavior
        ContentBehavior                   HeaderBehavior            FooterBehavior
    RefreshContentBehavior            RefreshHeaderBehavior      RefreshFooterBehavior

这个继承结构导致类型大量产生，通过分别对ContentBehavior,HeaderBehavior,FooterBehavior进行继承，目前RefreshContentBehavior，RefreshHeaderBehavior，RefreshFooterBehavior主要作用是通过监听器暴露滑动状态和刷新状态，以及作为控制器。这说明这部分职责可以考虑使用桥接的方式抽象到另一个层级，即和它们的父类一个层级。这样可以改用组合的方式在运行时改变其暴露出的行为，这样还可以将部分原来写死在Behavior中与滑动相关性不大的逻辑也让其负责，如行为模式的配置与变化。

为此将实现的部分的滑动转态转移和控制职责抽象为专门的Controller。

目前的继承层级：
                                    ViewOffsetBehavior
                                AnimationOffsetBehavior
    ScrollingContentBehavior                    VerticalIndicatorBehavior
RefreshContentBehavior              RefreshHeaderBehavior           RefreshFooterBehavior


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

目前ScrollerListener接口暴露出的滑动是经过坐标系转换的，也就是可以对Header和Footer采用一致的状态转换设计。但Content、Header、Footer仅关心各自身的滑动，并且由于Header和Footer的使用方式，它们可能在静止和跟随之间任意切换，也就是其滑动状态是不确定的，而作为为滑动的中心Content总是移动的并且同时记录头部和脚部的滑动状态，因此所有状态相关的转移和控制由Content来实现，Header和Footer将RefreshListener全都Refresher委托给Content。

采用这种实现需要在Content中区分Header和Footer两者的滑动状态，从而分别进行状态转换和控制。

根据监听到的滑动事件进行状态转移的设计，目前一个完整的刷新状态集合如下：
```java
    static final int STATE_IDLE = 0;                     // 初始状态
    static final int STATE_START = 1;                    // 开始
    static final int STATE_READY = 2;                    // 准备
    static final int STATE_CANCELLED = 3;                // 取消
    static final int STATE_CANCELLED_RESET = 4;          // 取消并重置
    static final int STATE_REFRESH = 5;                  // 刷新
    static final int STATE_REFRESH_RESET = 6;            // 刷新中并重置
    static final int STATE_COMPLETE = 7;                 // 完成
    static final int STATE_IDLE_RESET = 8;               // 初始并重置
```

## 刷新状态机的问题与改进
对于已经进入到状态3，4，7，8的情况会自动进回到IDLE状态，由于滑动还在继续，仍然会导致新的状态转移，而实际上是不需要进行多余的状态转换的。

