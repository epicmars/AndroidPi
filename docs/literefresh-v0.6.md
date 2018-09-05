---
title: LiteRefresh v0.6
date: 2018-09-03 06:48:28 +0800
---

## 对Margin的支持
### Indicator初始可见性
- Header
    + 如果Header高度大于0
        + 如果Header完全可见，将top与bottom Margin加入到其中
        + 如果Header部分可见，将bottom margin加入到其中
        + 如果Header不可见，则不算入margin
    + 如果Header高度等于0
        + 认为Header不可见，不算入margin
- Footer：类似，将top，bottom互换即可

### Content的测量
目前参与Content测量的有其minOffset，由于除了该限制，Header和Footer均可以滑到屏幕外部，因此Content需要能够占用所有其余剩余空间才能正常工作。

## 对Padding的支持
Padding用于限制View内部的内容的边距，因此不影响滑动偏移，不需要进行特定的支持。

## 对较短Content的支持
- 对于Header保持不变
- 对于Footer，在计算初始可见高度时，除了配置的可见高度，还需将Content占据的高度和Header的初始可见高度累加后的剩余高度进行计算，取两者中的较大值作为其最终的初始可见高度
### Content重置
- 如果Footer已经处于可见的状态

    重置Footer到初始可见高度

- 如果Footer不可见

    重置Header到初始可见高度