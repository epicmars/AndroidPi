## HeaderBehavior
作为NestedScrollChild，由于NestedScrollView与RecyclerView嵌套滑动分发逻辑不一致，仅允许Header固定。
### 效果
1. 经典模式，头部位于内容上，默认不可见

### 需求
1. 可见高度
设定一个可见性高度visibleHeight，它可以用于计算固定偏移量fixedOffset。默认Header不可见，那么固定偏移量是View高度的负值，也是最小值，如果完全可见，该偏移量为0。

    fixedOffset = -childHeight + visibleHeight;

2. 最大偏移量
最大偏移量可以设置为父View高度的一个百分比值，然后与Header自身高度减去visibleHeight相比，取较大的值。

#### 布局
1. 首次布局设置初始位置，其它时刻布局保留偏移值

#### 滑动
1. Header跟随触摸滑动进行移动并更新偏移值
    + 偏移值的消费方式可以进行自定义
    + 向上滑动时，消费的偏移值为负，此时偏移量区间为[0, bottom-visibleHeight]，取其负值为[-bottom + visibleHeight, 0]
    + 向下滑动时，消费的偏移值为正
        - 最小为0
        - 最大值可以进行自定义
            + 可以是子View的高度（子View高度可以根据偏移进度进行改变）
            + 可以是父布局的任意比例

#### 投掷（fling）
HeaderBehavior对Fling事件的消费方式。

#### 进度的消费
默认直接消费偏移值，并添加到View的偏移中，子类可以对这一行为进行自定义，但onNestedPreScroll中已消费的值总是直接累加原始偏移值。

## RefreshHeaderBehavior
### 刷新状态
#### 状态转换
1. 达到View初始高度以及ScrollView正文区域的高度（即父布局的高度）中的较小值，最大为两者中较大值
2. 如果正在刷新中，则刷新完成前不接受新的刷新请求
3. 滑动停止时如果刷新完成或者直接停止刷新则采用动画自动复位
    + 如果需要停顿则停顿后再复位
    + 否则直接复位