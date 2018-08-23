---
title: LiteRefresh v0.3
date: 2018-08-21 19:07:17 +0800
---

关于Header、Footer与Content的组合有几种情况，其中Content作为滑动View总是可以进行滑动，但滑动范围可以做出限制，首先分别列出两者的可能需求：

- Header：
    1. 位置偏移（topBottomOffset）
        + 跟随Content滑动
        + 不跟随
        + 偏移模式可以动态地改变

    2. 默认可见度
        + 部分可见
        + 完全可见，部分可见的一个特例
        + 完全不可见，部分可见的一个特例（此时，界面可观察到的仅有Content，那么Header必定是跟随Content，从而可以经过偏移变得可见，并且Content不需要与其重叠，它会导致Content部分内容被隐藏。假设有重叠，这时，上拉过程中被隐藏的内容无法被观察到，下拉过程中，达到可见的Content偏移量为0，这和没有重叠的情形没有区别，因此这样做的作用是默认隐藏部分Content的内容）
- Content：
    1. 位置偏移（topBottomOffset）
        + 可见度偏移量，用于设定Header和Footer的可见度
        + 最大偏移量，用于设定Content最大偏移量
        + 一个minOffset偏移量值，Content在达到该偏移量时停止偏移量的滑动，Header的跟随模式决定了是否发生重叠

    2. 高度（由于可以添加新的内容，它可以动态地变化）
        + Content的高度大于等于父布局高度
        + Content的高度小于父布局的高度
            + Header与Content可见度之和小于等于父布局高度（此时重叠就没有意义）
            + Header与Content可见度之和大于父布局高度

- Footer（基本上和Header是一样的）

## 需求的组合
1. Header固定不变，Content不能与其重叠（可以使用一般布局做到，或作使用RecyclerView）
2. Header固定不变，Content可以与其重叠

## 0.1版本实现中的问题
1. Content同时依赖于Header和Footer，对其位置变化的协调较难统一，可能需要Header和Footer进行相互的引用，或者使用一个统一的协调者。
2. 实现重叠与跟随两种模式时
    + 不重叠
        此时Content依赖Header，跟随Header移动。
        Header需要消费Content未消费的滑动。
    + 重叠
        此时Content需要实现自有的滑动逻辑，向下时消费自身未消费的滑动，并且需要使用Header的可见高度来限制滑动距离，向上时消费未消费的滑动。

        Header需要根据Content的偏移量来确定是否已经发生重叠，从而判断是否去消费Content未消费的滑动，如果重叠则由Contenet来消费，否则下滑时由Header来消费。
        
        由于两者都可能消费Content未消费的滑动，这种协同逻辑分散在两个不同类中，并且引入相互依赖的强耦合。

## 0.2版本的改进
1. 使用Content作为滑动的轴心，Header和Footer将其设置为依赖，那么Header和Footer不用了解对方。

    这里Content作为滑动的核心，承载原来Header的部分职责，主要是偏移量相关的逻辑，如最大偏移量。

    Header依赖于Content，可以根据Content的偏移量进行变化，同时Header可以通过CoordinatorLayout的依赖图获取到Content关联的View。

2. 实现重叠与不重叠两种模式
    - 不重叠
        + Content滑动时消费其未消费的嵌套滑动，从而改变其自身偏移量
        + Header在Content偏移量改变时，跟随改变
    - 重叠
        + Content滑动时消费其未消费的嵌套滑动，从而改变其自身偏移量
        + Header在Content偏移量改变时，不跟随

## 0.3版本修正
1. 从0.2版本可以看到重叠的逻辑实际上是由Header完成的，并且它与Header的可见度相关，并且实现上是通过改变Header偏移量完成的，**因此它属于Header的一部分**。并且由于Header是知晓Content的Behavior和View的，可以根据Content的变化实现重叠模式的动态变化。无论Header是固定还是跟随都可以重叠或不重叠。

2. 关于Header偏移量固定与跟随Content的以及其与Content重叠与否问题：
    - 偏移量固定
        + 重叠，即不跟随，对偏移量跟随程度为0
        + 不重叠，那么Content需要保证Header的可见，在达到可见度偏移时停止滑动

    - 偏移量跟随Content
        + 重叠，那么实际上和偏移量固定没有区别，如果有可以将偏移量的跟随程度作为一个度量，表示此时对偏移量的跟随程度为0
        + 不重叠，如果需要保证Header的可见，那么实际上和偏移量固定也没有区别，否则才是真正的跟随Content的滑动

    因此这里采用偏移量固定与偏移量跟随的划分，根据0.2版第2条，不重叠隐含跟随，重叠隐含位置固定。因此，采用偏移量固定与跟随的划分在重叠与否方面存在冲突（交集），其交集有两个：

    1. 重叠的情况
    2. 不重叠并且保证Header固定可见

    而重叠与不重叠来进行划分，并且偏移量固定与跟随可以用一个跟随程度来表示，那么就可以进行如下需求划分：

    - 重叠：
        + 根据跟随程度可以达到完全重叠，和部分重叠。如果跟随程度为1那么其效果就是不重叠
    - 不重叠：
        + 保证Header可见，Content在达到可见度偏移时停止偏移量的滑动，Header完全的跟随
        + 无需保证Header可见，Content可以滑动到父布局最上方

    这里重叠与不重叠之间也存在交集：

    1. 无需保证Header可见，Content可以滑动到父布局最上方，只是跟随程度不同而已，跟随程度为1表示不重叠，跟随程度为[0, 1)表示重叠的情况

    实际上，保证Header可见与重叠并不冲突，如果Header的跟随程度小于1，那么也是可行的，此时可以发生部分重叠，因此可以再次进行划分：

    - 保证Header可见，Content在达到可见度偏移时停止偏移量的滑动，Header的跟随程度决定了是否发生重叠
    - 不保证Header可见，Content可以滑动到父布局的最上方，Header的跟随程度决定了是否发生重叠


> 注意：这里的二元划分实际上都被转成了一个连续量。
        

3. 关于Header的可见度的需求，由于重叠的需求，Header是否可见是与Content的偏移相关的，如果按照0.2版本中将其由Header来负责，那么需要Content进行配合。由于可见性高度是Header所负责，如果Content要承担这部分逻辑，反过来需要与Header进行交互。这里不希望引入这种依赖，**那么将可见度的设置由Content进行设置即可**，然后Header获取可见度，并进行的相应的布局和偏移量的设置。

这里在原来的基础上由Content设定一个可见度偏移量，实际上相当于未设置可见度时的默认可见度偏移量为0，默认偏移量的作用如下：

- 布局时设置View位置与初始偏移量
- 限制Content滑动位置，原则上是未作限制的，默认限制为父布局的坐标系。根据Header的位置偏移的情况，Content的滑动需要考虑Header是否可见，以及可见高度的设定，即：
    + 保证Header可见，Content在达到可见度偏移时停止偏移量的滑动
    + 不保证Header可见，Content可以滑动到父布局的最上方

至此，可以发现对于Header是否可见的设置应该由Content负责，而Header仅负责设置一个跟随程度即可。

4. Footer的实现，由于其需求与Header一致，完全可以使用同一个超类进行实现共有的逻辑，具体实现使用坐标系转换即可。

5. 对于Header跟随程度的设定，需要知道Content的滑动距离，而CoordinatorLayout在被依赖的View变化时并没有给出变化的值，如果需要，需要去记录上一次的偏移量，这是可以实现的，因为已经记录了top和bottom的偏移量。但这样做会增加API的使用成本。最重要的一点是，Header与Content这种交错滑动的动态效果使用Transition来实现更为合适。因此跟随程度改回原来的设计，即跟随与不跟随两种。

6. 对于Content的滑动偏移量的最小值限制，采用保证Header可见与不可见两种模式可以合并为一个线性值minOffset，默认由Header测量完成时获取Content并将该值设置为Header自身的visibleHeight。

7. Header与Footer坐标系

Header与Footer跟随Content移动时需要通过监听器报告其滑动的距离，滑动距离总是正值，因此它们和View的坐标系不一致。但Header和Footer仅滑动方向相反它们两者的坐标系可以通过坐标系的矩阵转换实现。

首先列出滑动点在各自坐标系中的表示，由于仅考虑垂直方向的滑动，只需关心垂直方向上y坐标的变化：

根据ViewOffsetHelper记录的topBottomOffset，由于CoordinatorLayout相当于一个FrameLayout，该偏移量的初始位置位于左上角位置：

y = topBottomOffset

height = child.getHeight()

parentHeight = parent.getHeight()

- Header：滑动点为View的Bottom
    + Parent坐标系：y
    + Header坐标系：y + height

- Footer：滑动点为View的Top
    + Parent坐标系：y
    + Footer坐标系：parentHeight - y

Parent的向量空间转换为Header的向量空间：

     |1 0 height||x|   |         x|
     |0 1 height||y| = |y + height|
     |0 0      1||1|   |         1|

Parent的向量空间转换为Footer向量空间：
     |1   0              0||x|   |                x|
     |0   -1  parentHeight||y| = |-y + parentHeight|
     |0   0              1||1|   |                1|

## RefreshBehavior状态变化
目前采用如下接口监听滑动事件，需要注意onStopScroll仅表示滑动触摸操作的结束，可能对应TouchEvent.UP事件，但不表明滑动的结束，随后可能使用延时的动画来更新视图：
```java
    public interface ScrollListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int max, boolean isTouch);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int max, boolean isTouch);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int max, boolean isTouch);
    }
```

根据监听到的滑动事件进行状态转移的设计，目前一个完整的刷新状态集合如下：
```java
    private static final int STATE_IDEL = 0;        // 初始状态
    private static final int STATE_START = 1;       // 开始刷新，发生触摸事件
    private static final int STATE_READY = 2;       // 达到刷新触发条件
    private static final int STATE_REFRESH = 3;     // 正在刷新
    private static final int STATE_COMPLETE = 4;    // 刷新结束
```
