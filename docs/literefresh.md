LiteRefresh
===============
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

    2. 高度（由于可以添加新的内容，它可以在动态地变化）
        + Content的高度大于等于父布局高度
        + Content的高度小于父布局的高度
            + Header与Content可见度之和小于等于父布局高度（此时重叠就没有意义）
            + Header与Content可见度之和大于父布局高度

## 需求的组合
1. Header固定不变，Content不能与其重叠（可以使用一般布局做到，或作使用RecyclerView）
2. Header固定不变，Content可以与其重叠


