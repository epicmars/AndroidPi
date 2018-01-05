package cn.androidpi.app.ui.fragment

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
import android.widget.Toast
import cn.androidpi.app.R
import cn.androidpi.app.databinding.FragmentNewsBinding
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.app.ui.base.RecyclerAdapter
import cn.androidpi.app.ui.viewholder.CoverNewsViewHolder
import cn.androidpi.app.ui.viewholder.ErrorViewHolder
import cn.androidpi.app.ui.viewholder.NewsThreeImagesViewHolder
import cn.androidpi.app.ui.viewholder.NewsViewHolder
import cn.androidpi.app.ui.viewholder.items.ErrorItem
import cn.androidpi.app.view.NewsView
import cn.androidpi.app.viewmodel.NewsViewModel
import cn.androidpi.app.widget.*
import cn.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_news)
class NewsFragment : BaseFragment<FragmentNewsBinding>(), NewsView {

    lateinit var mNewsModel: NewsViewModel

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mAdapter: RecyclerAdapter

    var mPortal: String? = null

    companion object {

        val KEY_PORTAL = "NewsFragment.KEY_PORTAL"

        fun newInstance(portal: String?): NewsFragment {
            val args = Bundle()
            args.putString(KEY_PORTAL, portal)
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If retainInstance is set to be true, on configuration change,
        // onCreate will not be called, therefore [NewsViewModel] will not be recreated.
        mNewsModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(NewsViewModel::class.java)
        mPortal = arguments?.getString(KEY_PORTAL)
        mNewsModel.mPortal = mPortal
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
        mAdapter.setFragmentManager(childFragmentManager)
        mAdapter.register(CoverNewsViewHolder::class.java,
                NewsViewHolder::class.java,
                NewsThreeImagesViewHolder::class.java,
                ErrorViewHolder::class.java)
        mBinding.recyclerNews.adapter = mAdapter

        //
        val lpHeader = mBinding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        if (lpHeader.behavior == null) {
            lpHeader.behavior = PullDownHeaderBehavior<View>()
        }
        val behavior = lpHeader.behavior as PullDownHeaderBehavior
        behavior.addOnPullingListener(object : OnPullingListener {

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

        })
        behavior.addOnRefreshListener(object : OnRefreshListener {

            override fun onRefreshStart() {
            }

            override fun onRefreshReady() {
            }

            override fun onRefresh() {
                loadFirstPage()
            }

            override fun onRefreshComplete() {
            }
        })
        lpHeader.behavior = behavior
        mBinding.scrollHeader.layoutParams = lpHeader

        val lpFooter = mBinding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        val footerBehavior = PullUpFooterBehavior<View>(context)
        footerBehavior.addOnRefreshListener(object : OnRefreshListener {

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

        footerBehavior.addOnPullingListener(object : OnPullingListener {
            override fun onPulling(current: Int, delta: Int, max: Int) {
            }

            override fun onStartPulling(max: Int) {
                footerBehavior.refresh()
            }

            override fun onStopPulling(current: Int, max: Int) {
            }
        })

        lpFooter.behavior = footerBehavior
        mBinding.scrollFooter.layoutParams = lpFooter

        val lpScroll = mBinding.recyclerNews.layoutParams as CoordinatorLayout.LayoutParams
        lpScroll.behavior = ScrollViewBehavior<View>()
        mBinding.recyclerNews.layoutParams = lpScroll

        // pull up to load more
        mBinding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            val THRESHOULD = PAGE_SIZE / 2

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy == 0 || footerBehavior.isRefreshing)
                    return
                val lastVisibleItem = (recyclerView?.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                val totalItemCount = recyclerView.layoutManager.itemCount
                if (totalItemCount <= lastVisibleItem + THRESHOULD) {
                    footerBehavior.refresh()
                }
            }
        })

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNewsModel.mNews.observe(this, Observer { t ->
            refreshFinished()
            if (t == null || t.isError()) {
                val message = if (t != null) t.message else "加载失败"
                if (t?.data != null && t.data.isFirstPage()) {
                    mAdapter.setPayloads(ErrorItem(message))
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } else {
                val currentPage = t.data?.mCurrentPage
                if (currentPage == null || currentPage.isEmpty())
                    return@Observer
                if (t.data.isFirstPage()) {
                    mAdapter.setPayloads(currentPage)
                } else {
                    mAdapter.appendPayloads(t.data.mPreviousPages, currentPage)
                }
            }
        })
        if (null == savedInstanceState || mNewsModel.mNews.value == null || mNewsModel.mNews.value!!.data!!.isFirstPage()) {
            loadFirstPage()
        }
    }

    fun refreshFinished() {
        val lpHeader = mBinding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = lpHeader.behavior as PullDownHeaderBehavior
        behavior.refreshComplete()

        val lpFooter = mBinding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        val footerBehavior = lpFooter.behavior as PullUpFooterBehavior
        footerBehavior.refreshComplete()
    }

    override fun loadFirstPage() {
        mNewsModel.refreshPage()
    }

    override fun loadNextPage() {
        mNewsModel.nextPage()
    }
}