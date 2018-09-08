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
            add(LiteRefreshSample("Movies", "Movie data from The Movie Db.", MoviePagerFragment::class.java))
            add(LiteRefreshSample("Collapsible header.", "A scrollable list with a collapsible header.", CollapsibleHeaderFragment::class.java))
            add(LiteRefreshSample("Partial visible RecyclerView.", "A recycler view that is partial visible.", PartialVisibleListFragment::class.java))
            add(LiteRefreshSample("Partial visible header.", "A recycler view with a partial visible header.", PartialVisibleHeaderFragment::class.java))
            add(LiteRefreshSample("Header follow with content.", "Header can follow up and down with content.", HeaderFollowFragment::class.java))
            add(LiteRefreshSample("Header follow up.", "Header can follow up with content but not down.", HeaderFollowUpFragment::class.java))
            add(LiteRefreshSample("Header follow down.", "Header can follow down with content but not up", HeaderFollowDownFragment::class.java))
            add(LiteRefreshSample("Header stays still.", "Header doesn't follow down or up with content.", HeaderStillFragment::class.java))
        }
    }
}
