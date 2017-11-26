package cn.androidpi.app.components.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.components.base.RecyclerAdapter
import cn.androidpi.app.components.viewholder.NewsViewHolder
import cn.androidpi.app.databinding.FragmentNewsBinding
import cn.androidpi.app.view.NewsView
import cn.androidpi.app.viewmodel.CoverNewsViewModel
import cn.androidpi.app.viewmodel.NewsViewModel
import cn.androidpi.app.widget.PullDownHeaderBehavior
import cn.androidpi.app.widget.PullUpFooterBehavior
import cn.androidpi.app.widget.PullingListener
import cn.androidpi.app.widget.ScrollViewBehavior
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_news)
class NewsFragment : BaseFragment<FragmentNewsBinding>(), NewsView {

    lateinit var mNewsModel: NewsViewModel

    lateinit var mCoverNewsModel: CoverNewsViewModel

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mAdapter: RecyclerAdapter

    lateinit var mNewsCoverAdapter: CoverNewsPageAdapter

    companion object {
        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If retainInstance is set to be true, on configuration change,
        // onCreate will not be called, therefore [NewsViewModel] will not be recreated.
        mNewsModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(NewsViewModel::class.java)
        mCoverNewsModel = ViewModelProviders.of(activity!!).get(CoverNewsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // If retainInstance is set to be true, on configuration change,
        // View state will be restored,
        // thus the referenced obsolete activity context should not be used any more,
        // otherwise a memory leak may occur.
        mBinding.recyclerNews.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerNews.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        mAdapter = RecyclerAdapter()
        mAdapter.register(NewsViewHolder::class.java)
        mBinding.recyclerNews.adapter = mAdapter
        // pull down to refresh
//        mBinding.swipeRefresh.setOnRefreshListener({
//            loadFirstPage()
//        })

        // pull up to load more
        mBinding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                val lastVisibleItem = (recyclerView?.layoutManager as LinearLayoutManager)
//                        .findLastVisibleItemPosition()
//                val totalItemCount = recyclerView?.layoutManager.itemCount
//                if (lastVisibleItem >= totalItemCount - 6) {
//                    loadNextPage()
//                }
            }
        })
        //
        val lpHeader = mBinding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        if (lpHeader.behavior == null) {
            lpHeader.behavior = PullDownHeaderBehavior<View>()
        }
        val behavior = lpHeader.behavior as PullDownHeaderBehavior
        behavior.addPullDownListener(object : PullingListener {

            override fun onStartPulling(max: Int) {
//                mBinding.pullingProgress.visibility = View.VISIBLE
//                mBinding.pullingProgress.max = max
            }

            override fun onPulling(current: Int, delta: Int, max: Int) {
//                mBinding.pullingProgress.setProgress(current)
            }

            override fun onStopPulling(current: Int, max: Int) {
//                mBinding.pullingProgress.visibility = View.GONE
            }

            override fun onRefreshStart() {
            }

            override fun onRefreshReady() {
            }

            override fun onRefresh() {
                refreshWithLoading()
            }

            override fun onRefreshComplete() {
            }
        })
        lpHeader.behavior = behavior
        mBinding.scrollHeader.layoutParams = lpHeader

        val lpFooter = mBinding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        val footerBehavior = PullUpFooterBehavior<View>(context)
        footerBehavior.addPullUpListener(object : PullingListener {

            override fun onStartPulling(max: Int) {
            }

            override fun onPulling(current: Int, delta: Int, max: Int) {

            }

            override fun onStopPulling(current: Int, max: Int) {

            }

            override fun onRefreshStart() {
            }

            override fun onRefreshReady() {
            }

            override fun onRefresh() {
                loadNextPage()
            }

            override fun onRefreshComplete() {
            }
        })

        lpFooter.behavior = footerBehavior
        mBinding.scrollFooter.layoutParams = lpFooter

        val lpScroll = mBinding.newsContent.layoutParams as CoordinatorLayout.LayoutParams
        lpScroll.behavior = ScrollViewBehavior<View>()
        mBinding.newsContent.layoutParams = lpScroll

        mBinding.recyclerNews.isFocusable = false
        mBinding.recyclerNews.isNestedScrollingEnabled = false

        // news cover pager
        mNewsCoverAdapter = CoverNewsPageAdapter(fragmentManager, mCoverNewsModel)
        mBinding.newsPager.adapter = mNewsCoverAdapter
        mBinding.newsPager.offscreenPageLimit = 2
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNewsModel.mNews.observe(this, Observer { t ->
            refreshFinished()
            val currentPage = t?.mCurrentPage
            if (currentPage == null || currentPage.isEmpty())
                return@Observer
            if (t.isFirstPage()) {
                val coverImagesSize = t.mCoverNews.size
                if (coverImagesSize <= 0) {
                    mBinding.newsPager.visibility = View.GONE
                } else {
                    mBinding.newsPager.visibility = View.VISIBLE
                    mNewsCoverAdapter.setCoverNews(t.mCoverNews)
                }
                mAdapter.setPayloads(currentPage)
            } else {
                mAdapter.appendPayloads(t.mPreviousPages, currentPage)
            }
        })
        if (null == savedInstanceState) {
            refreshWithLoading()
        }
    }

    fun refreshWithLoading() {
        loadFirstPage()
    }

    fun refreshFinished() {
        val lpHeader = mBinding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = lpHeader.behavior as PullDownHeaderBehavior
        behavior.refreshComplete()
    }

    override fun loadFirstPage() {
        mNewsModel.refreshPage()
    }

    override fun loadNextPage() {
        mNewsModel.nextPage()
    }
}