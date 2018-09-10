---
title: LiteRefresh v0.7
date: 2018-09-05 21:53:35 +0800
---

## 对实例状态保存与恢复的支持
### Content位置的初始化
首次布局需要将Content设置为Header的初始可见高度，恢复状态时需要将其恢复为保存的初始偏移量，但布局的会进行多次，如何保证正确地设置初始偏移量？

- 在首次布局过程中初始偏移量是一个无效值，那么其行为是正确的。
- 一旦状态进行了恢复，初始偏移量的值是有效的，由于Footer的初始可见高度需要根据Content的长度进行动态计算，那么一旦Content引起Footer布局位置改变，那么会导致重新配置，这时不应当使用已经过时的初始偏移量。并且由于Content加载内容过程中高度会发生变化，所引起Footer布局时初始长度的计算次数不确定。

两者的区别在于一个是首次布局，一个非首次布局，首次布局时未触发滑动操作，而非首次布局则已经滑动过了，因此一旦Content的位置由于滑动导致变化，那么此时恢复的初始偏移量不再有效。

## 问题
1. Content位置恢复的问题（todo）
对于PartialVisibleHeaderFragment，如果设置不保留活动，并且通过下拉进入二楼跳转到新的页面，返回时由于状态恢复导致新的页面跳转被触发。

2. 在状态恢复过程中重新创建了实例，此时Content在首次布局中先于Header和Footer进行恢复并设置偏移量，这时会引起Header或Footer的onDependentViewChanged被回调并相应地进行偏移量的设置，但这时Header和Footer还未进行布局，此时parent和child还未初始化，导致空指针异常。(solved)

3. 在Movie详情页中，图像加载异常的问题。
(partially solved 2018-09-10 21:51:45) 将adjustViewBound去掉可以解决该问题
(solved 2018-09-11 00:09:57) 由于布局时高度发生变化，需要重新设置偏移量

4. 接口优化（todo）
对于BehaviorConfiguration的使用，Header与Content的配置如何暴露与使用的问题，例如Header和Footer分别有初始可见高度和触发偏移量，这两个配置对Content的配置而言没有实际意义。

5. LoadingView状态异常，无网络情况下Weather的LoadingView在加载完成后未复位。
(solved 2018-09-10 21:32:02)