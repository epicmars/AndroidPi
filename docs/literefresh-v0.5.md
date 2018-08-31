---
title: LiteRefresh v0.5
date: 2018-08-27 17:53:18 +0800
---

## 偏移量的设定与协作
### Header与Footer
目前设定Header与Footer有一个可见高度，这个高度用于限定Content重置（以及首次布局）的锚点。

Content最大最小偏移量的关系用于限定Content滑动范围。

### 触发刷新的偏移量
## 目前的设计以及问题
如果Header完全可见即触发。

- 问题在于如果Header已经是完全可见的，那么只要触摸即触发。
- 如果Header完全不可见或者部分可见，并且不可见的部分过长，那么下拉到完全可见不太友好。

## 改进
- 用户自定义了一个触发刷新的偏移量
    + 如果偏移量为无效的值，即负数，那么使用默认偏移量
    + Indicator完全不可见，使用自定义偏移量
    + Indicator部分可见，使用自定义偏移量
    + Indicator完全可见，使用自定义偏移量
- 用户未定义触发偏移量范围
    + Indicator完全不可见
        + visibleHeight为0，那么默认使Indicator完全可见可触发刷新
        + visibleHeight为负，那么默认使Indicator完全可见可触发刷新
    + Indicator部分可见，那么默认使Indicator完全可见触发刷新，如果不可见部分过小，会导致误触发，设定一个默认偏移量defaultRefreshRange，所有偏移量不能比它小。
    + Indicator完全可见使用一个固定的默认偏移量defaultRefreshTriggerRange


