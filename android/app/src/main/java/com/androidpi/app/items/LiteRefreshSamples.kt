package com.androidpi.app.items

import com.androidpi.app.fragment.*

import java.util.ArrayList

/**
 * Created by jastrelax on 2018/8/19.
 */
object LiteRefreshSamples {

    val samples: List<LiteRefreshSample> = object : ArrayList<LiteRefreshSample>() {
        init {
            add(LiteRefreshSample("News", "A news feed.", NewsFragment::class.java))
            add(LiteRefreshSample("Unsplash", "An gallery of photos from unsplash.", UnsplashFragment::class.java))
            add(LiteRefreshSample("Image Pager", "A scrollable list with a nested view pager.", ImagePagerFragment::class.java))
            add(LiteRefreshSample("Profile", "A scrollable profile page.", ProfileFragment::class.java))
            add(LiteRefreshSample("Header Transition", "Header transition.", HeaderTransitionFragment::class.java))
            add(LiteRefreshSample("Second Floor", "Second floor like taobao home page.", SecondFloorFragment::class.java))
        }
    }
}
