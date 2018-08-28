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
            add(LiteRefreshSample("Partial visible list.", "List with a partial visible header.", PartialVisibleListFragment::class.java))
            add(LiteRefreshSample("Header partial visible.", "A list with a partial visible header.", PartialVisibleHeaderFragment::class.java))
            add(LiteRefreshSample("Fade in and out header.", "A scrollable list with a fade in and out collapsible header.", FadingHeaderFragment::class.java))
            add(LiteRefreshSample("Header follow up.", "Header can follow up with content but not down.", HeaderFollowUpFragment::class.java))
            add(LiteRefreshSample("Header follow down.", "Header can follow down with content but not up", HeaderFollowDownFragment::class.java))
            add(LiteRefreshSample("Header stays sill.", "Header doesn't follow down or up with content.", HeaderStillFragment::class.java))
            add(LiteRefreshSample("Header Transition", "Header transition.", HeaderTransitionFragment::class.java))
            add(LiteRefreshSample("Second Floor", "Second floor like taobao home page.", SecondFloorFragment::class.java))
        }
    }
}
