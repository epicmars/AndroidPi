---
title: LiteRefresh v0.2
date: 2018-08-20 19:07:17 +0800
---

关于Header与Content的组合有几种情况，其中Content作为滑动View总是可以进行滑动，但滑动范围可以做出限制，首先分别列出两者的可能需求：

- Header：
    1. 位置偏移（topBottomOffset）
        + 固定不变
        + 跟随Content（触发动作，如scale，进度展示）
    2. 默认可见度
        + 部分可见
        + 完全可见，部分可见的一个特例
        + 完全不可见，部分可见的一个特例（此时，界面可观察到的仅有Content，那么Header必定是跟随Content，从而可以经过偏移变得可见，并且Content不需要与其重叠，它会导致Content部分内容被隐藏。假设有重叠，这时，上拉过程中被隐藏的内容无法被观察到，下拉过程中，达到可见的Content偏移量为0，这和没有重叠的情形没有区别，因此这样做的作用是默认隐藏部分Content的内容）
- Content：
    1. 位置偏移（topBottomOffset）
        + 可与Header重叠（仅与Header可见的部分发生重叠，与不可见的部分发生重叠的原因同上）
        + 不可与Header重叠（两者不重叠，如果Header位置固定，那么可以使用普通布局实现，如果Header跟随Content那么可以使用整个RecyclerView实现）
        + 是否重叠可以动态变化（如根据Content高度的变化）

    2. 高度（由于可以添加新的内容，它可以动态地变化）
        + Content的高度大于等于父布局高度
        + Content的高度小于父布局的高度
            + Header与Content可见度之和小于等于父布局高度（此时重叠就没有意义）
            + Header与Content可见度之和大于父布局高度

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

2. 实现重叠与跟随两种模式
    - 不重叠
        + Content滑动时消费其未消费的嵌套滑动，从而改变其自身偏移量
        + Header在Content偏移量改变时，跟随改变
    - 重叠
        + Content滑动时消费其未消费的嵌套滑动，从而改变其自身偏移量
        + Header在Content偏移量改变时，不跟随