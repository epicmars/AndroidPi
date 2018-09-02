package com.androidpi.app.base.widget.literefresh;

/**
 * Created by jastrelax on 2018/8/30.
 */
public class BehaviorConfiguration {

    // common settings
    private int maxOffset = 0;
    private float maxOffsetRatio = 0f;
    private float maxOffsetRatioOfParent = 0f;
    private boolean useDefaultMaxOffset = false;
    private int height;
    private int parentHeight;

    // indicators settings
    private boolean isSettled;
    private int visibleHeight = 0;
    private int invisibleHeight = 0;
    private float visibleHeightRatio = 0f;
    private float visibleHeightParentRatio = 0f;
    private boolean useDefinedRefreshTriggerRange = false;
    private int defaultRefreshTriggerRange;
    private int refreshTriggerRange;
    private Boolean showUpWhenRefresh;

    // content settings
    /**
     * Minimum top and bottom offset of content view.
     */
    private int minOffset;
    private int cachedMinOffset;

    /**
     * Minimum top and bottom offset relative to parent height.
     */
    private float minOffsetRatio;
    private float minOffsetRatioOfParent;
    private boolean useDefaultMinOffset;

    public BehaviorConfiguration() {
    }

    public BehaviorConfiguration(Builder builder) {
        this.maxOffset = builder.maxOffset;
        this.maxOffsetRatio = builder.maxOffsetRatio;
        this.maxOffsetRatioOfParent = builder.maxOffsetRatioOfParent;
        this.useDefaultMaxOffset = builder.useDefaultMaxOffset;
        this.height = builder.height;
        this.parentHeight = builder.parentHeight;

        this.isSettled = builder.isSettled;
        this.visibleHeight = builder.visibleHeight;
        this.invisibleHeight = builder.invisibleHeight;
        this.visibleHeightRatio = builder.visibleHeightRatio;
        this.visibleHeightParentRatio = builder.visibleHeightParentRatio;
        this.useDefinedRefreshTriggerRange = builder.useDefinedRefreshTriggerRange;
        this.defaultRefreshTriggerRange = builder.defaultRefreshTriggerRange;
        this.refreshTriggerRange = builder.refreshTriggerRange;
        this.showUpWhenRefresh = builder.showUpWhenRefresh;

        this.minOffset = builder.minOffset;
        this.cachedMinOffset = builder.cachedMinOffset;
        this.minOffsetRatio = builder.minOffsetRatio;
        this.minOffsetRatioOfParent = builder.minOffsetRatioOfParent;

        this.useDefaultMinOffset = builder.useDefaultMinOffset;
    }

    public int getMaxOffset() {
        return maxOffset;
    }

    public float getMaxOffsetRatio() {
        return maxOffsetRatio;
    }

    public float getMaxOffsetRatioOfParent() {
        return maxOffsetRatioOfParent;
    }

    public boolean isUseDefaultMaxOffset() {
        return useDefaultMaxOffset;
    }

    public int getHeight() {
        return height;
    }

    public int getParentHeight() {
        return parentHeight;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }

    public int getInvisibleHeight() {
        return invisibleHeight;
    }

    public float getVisibleHeightRatio() {
        return visibleHeightRatio;
    }

    public float getVisibleHeightParentRatio() {
        return visibleHeightParentRatio;
    }

    public boolean isUseDefinedRefreshTriggerRange() {
        return useDefinedRefreshTriggerRange;
    }

    public int getDefaultRefreshTriggerRange() {
        return defaultRefreshTriggerRange;
    }

    public int getRefreshTriggerRange() {
        return refreshTriggerRange;
    }

    public int getMinOffset() {
        return minOffset;
    }

    public int getCachedMinOffset() {
        return cachedMinOffset;
    }

    public float getMinOffsetRatio() {
        return minOffsetRatio;
    }

    public float getMinOffsetRatioOfParent() {
        return minOffsetRatioOfParent;
    }

    public boolean isUseDefaultMinOffset() {
        return useDefaultMinOffset;
    }

    public void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    public void setMaxOffsetRatio(float maxOffsetRatio) {
        this.maxOffsetRatio = maxOffsetRatio;
    }

    public void setMaxOffsetRatioOfParent(float maxOffsetRatioOfParent) {
        this.maxOffsetRatioOfParent = maxOffsetRatioOfParent;
    }

    public void setUseDefaultMaxOffset(boolean useDefaultMaxOffset) {
        this.useDefaultMaxOffset = useDefaultMaxOffset;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }

    public void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
    }

    public void setInvisibleHeight(int invisibleHeight) {
        this.invisibleHeight = invisibleHeight;
    }

    public void setVisibleHeightRatio(float visibleHeightRatio) {
        this.visibleHeightRatio = visibleHeightRatio;
    }

    public void setVisibleHeightParentRatio(float visibleHeightParentRatio) {
        this.visibleHeightParentRatio = visibleHeightParentRatio;
    }

    public void setUseDefinedRefreshTriggerRange(boolean useDefinedRefreshTriggerRange) {
        this.useDefinedRefreshTriggerRange = useDefinedRefreshTriggerRange;
    }

    public void setDefaultRefreshTriggerRange(int defaultRefreshTriggerRange) {
        this.defaultRefreshTriggerRange = defaultRefreshTriggerRange;
    }

    public void setRefreshTriggerRange(int refreshTriggerRange) {
        this.refreshTriggerRange = refreshTriggerRange;
    }

    public void setMinOffset(int minOffset) {
        this.minOffset = minOffset;
    }

    public void setCachedMinOffset(int cachedMinOffset) {
        this.cachedMinOffset = cachedMinOffset;
    }

    public void setMinOffsetRatio(float minOffsetRatio) {
        this.minOffsetRatio = minOffsetRatio;
    }

    public void setMinOffsetRatioOfParent(float minOffsetRatioOfParent) {
        this.minOffsetRatioOfParent = minOffsetRatioOfParent;
    }

    public void setUseDefaultMinOffset(boolean useDefaultMinOffset) {
        this.useDefaultMinOffset = useDefaultMinOffset;
    }

    public Boolean getShowUpWhenRefresh() {
        return showUpWhenRefresh;
    }

    public void setShowUpWhenRefresh(Boolean showUpWhenRefresh) {
        this.showUpWhenRefresh = showUpWhenRefresh;
    }

    public static class Builder {

        // common settings
        private int maxOffset = 0;
        private float maxOffsetRatio = 0f;
        private float maxOffsetRatioOfParent = 0f;
        private boolean useDefaultMaxOffset = false;
        private int height;
        private int parentHeight;

        // indicators settings
        private int visibleHeight = 0;
        private int invisibleHeight = 0;
        private boolean isSettled;
        private float visibleHeightRatio = 0f;
        private float visibleHeightParentRatio = 0f;
        private boolean useDefinedRefreshTriggerRange = false;
        private int defaultRefreshTriggerRange;
        private int refreshTriggerRange;
        private Boolean showUpWhenRefresh;

        // content settings

        /**
         * Minimum top and bottom offset of content view.
         */
        private int minOffset;
        private int cachedMinOffset;

        /**
         * Minimum top and bottom offset relative to parent height.
         */
        private float minOffsetRatio;
        private float minOffsetRatioOfParent;
        private boolean useDefaultMinOffset;

        public Builder() {
        }

        public Builder(BehaviorConfiguration configuration) {
            if (configuration == null) return;
            this.maxOffset = configuration.maxOffset;
            this.maxOffsetRatio = configuration.maxOffsetRatio;
            this.maxOffsetRatioOfParent = configuration.maxOffsetRatioOfParent;
            this.useDefaultMaxOffset = configuration.useDefaultMaxOffset;
            this.height = configuration.height;
            this.parentHeight = configuration.parentHeight;

            this.isSettled = configuration.isSettled;
            this.visibleHeight = configuration.visibleHeight;
            this.invisibleHeight = configuration.invisibleHeight;
            this.visibleHeightRatio = configuration.visibleHeightRatio;
            this.visibleHeightParentRatio = configuration.visibleHeightParentRatio;
            this.useDefinedRefreshTriggerRange = configuration.useDefinedRefreshTriggerRange;
            this.defaultRefreshTriggerRange = configuration.defaultRefreshTriggerRange;
            this.refreshTriggerRange = configuration.refreshTriggerRange;
            this.showUpWhenRefresh = configuration.showUpWhenRefresh;


            this.minOffset = configuration.minOffset;
            this.cachedMinOffset = configuration.cachedMinOffset;
            this.minOffsetRatio = configuration.minOffsetRatio;
            this.minOffsetRatioOfParent = configuration.minOffsetRatioOfParent;
            this.useDefaultMinOffset = configuration.useDefaultMinOffset;
        }

        public Builder setMaxOffset(int maxOffset) {
            this.maxOffset = maxOffset;
            return this;
        }

        public Builder setMaxOffsetRatio(float maxOffsetRatio) {
            this.maxOffsetRatio = maxOffsetRatio;
            return this;
        }

        public Builder setMaxOffsetRatioOfParent(float maxOffsetRatioOfParent) {
            this.maxOffsetRatioOfParent = maxOffsetRatioOfParent;
            return this;
        }

        public Builder setUseDefaultMaxOffset(boolean useDefaultMaxOffset) {
            this.useDefaultMaxOffset = useDefaultMaxOffset;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setParentHeight(int parentHeight) {
            this.parentHeight = parentHeight;
            return this;
        }

        public Builder setVisibleHeight(int visibleHeight) {
            this.visibleHeight = visibleHeight;
            return this;
        }

        public Builder setInvisibleHeight(int invisibleHeight) {
            this.invisibleHeight = invisibleHeight;
            return this;
        }

        public Builder setSettled(boolean settled) {
            isSettled = settled;
            return this;
        }

        public Builder setVisibleHeightRatio(float visibleHeightRatio) {
            this.visibleHeightRatio = visibleHeightRatio;
            return this;
        }

        public Builder setVisibleHeightParentRatio(float visibleHeightParentRatio) {
            this.visibleHeightParentRatio = visibleHeightParentRatio;
            return this;
        }

        public Builder setUseDefinedRefreshTriggerRange(boolean useDefinedRefreshTriggerRange) {
            this.useDefinedRefreshTriggerRange = useDefinedRefreshTriggerRange;
            return this;
        }

        public Builder setDefaultRefreshTriggerRange(int defaultRefreshTriggerRange) {
            this.defaultRefreshTriggerRange = defaultRefreshTriggerRange;
            return this;
        }

        public Builder setRefreshTriggerRange(int refreshTriggerRange) {
            this.refreshTriggerRange = refreshTriggerRange;
            return this;
        }

        public Builder setShowUpWhenRefresh(Boolean showUpWhenRefresh) {
            this.showUpWhenRefresh = showUpWhenRefresh;
            return this;
        }

        public Builder setMinOffset(int minOffset) {
            this.minOffset = minOffset;
            return this;
        }

        public Builder setCachedMinOffset(int cachedMinOffset) {
            this.cachedMinOffset = cachedMinOffset;
            return this;
        }

        public Builder setMinOffsetRatio(float minOffsetRatio) {
            this.minOffsetRatio = minOffsetRatio;
            return this;
        }

        public Builder setMinOffsetRatioOfParent(float minOffsetRatioOfParent) {
            this.minOffsetRatioOfParent = minOffsetRatioOfParent;
            return this;
        }

        public Builder setUseDefaultMinOffset(boolean useDefaultMinOffset) {
            this.useDefaultMinOffset = useDefaultMinOffset;
            return this;
        }

        public int getMaxOffset() {
            return maxOffset;
        }

        public float getMaxOffsetRatio() {
            return maxOffsetRatio;
        }

        public float getMaxOffsetRatioOfParent() {
            return maxOffsetRatioOfParent;
        }

        public boolean isUseDefaultMaxOffset() {
            return useDefaultMaxOffset;
        }

        public int getHeight() {
            return height;
        }

        public int getParentHeight() {
            return parentHeight;
        }

        public int getVisibleHeight() {
            return visibleHeight;
        }

        public int getInvisibleHeight() {
            return invisibleHeight;
        }

        public boolean isSettled() {
            return isSettled;
        }

        public float getVisibleHeightRatio() {
            return visibleHeightRatio;
        }

        public float getVisibleHeightParentRatio() {
            return visibleHeightParentRatio;
        }

        public boolean isUseDefinedRefreshTriggerRange() {
            return useDefinedRefreshTriggerRange;
        }

        public int getDefaultRefreshTriggerRange() {
            return defaultRefreshTriggerRange;
        }

        public int getRefreshTriggerRange() {
            return refreshTriggerRange;
        }

        public Boolean getShowUpWhenRefresh() {
            return showUpWhenRefresh;
        }

        public int getMinOffset() {
            return minOffset;
        }

        public int getCachedMinOffset() {
            return cachedMinOffset;
        }

        public float getMinOffsetRatio() {
            return minOffsetRatio;
        }

        public float getMinOffsetRatioOfParent() {
            return minOffsetRatioOfParent;
        }

        public boolean isUseDefaultMinOffset() {
            return useDefaultMinOffset;
        }

        public BehaviorConfiguration build() {
            return new BehaviorConfiguration(this);
        }
    }

}
