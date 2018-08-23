package com.androidpi.app.fragment

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
import com.androidpi.app.R
import com.androidpi.app.base.di.Injectable
import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.base.ui.RecyclerAdapter
import com.androidpi.app.base.widget.literefresh.*
import com.androidpi.app.buiness.view.NewsView
import com.androidpi.app.buiness.viewmodel.NewsViewModel
import com.androidpi.app.databinding.FragmentNewsBinding
import com.androidpi.app.viewholder.ErrorViewHolder
import com.androidpi.app.viewholder.NewsViewHolder
import com.androidpi.app.viewholder.items.ErrorItem
import com.androidpi.news.model.NewsListModel.Companion.PAGE_SIZE
import com.androidpi.news.vo.NewsPagination
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_news)
@Injectable
class NewsFragment : BaseFragment<FragmentNewsBinding>(), NewsView {

    lateinit var mNewsModel: NewsViewModel

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mAdapter: RecyclerAdapter

    var mNewsCategory: String? = null

    companion object {

        val KEY_CATEGORY = "com.androidpi.app.fragment.NewsFragment.KEY_PORTAL"

        fun newInstance(): NewsFragment {
            return NewsFragment()
        }

        fun newInstance(category: String?): NewsFragment {
            val args = Bundle()
            args.putString(KEY_CATEGORY, category)
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
        mNewsCategory = arguments?.getString(KEY_CATEGORY)
        mNewsCategory = if (mNewsCategory == null) "general" else mNewsCategory
        mNewsModel.mCategory = mNewsCategory

        mNewsModel.mNews.observe(this, Observer { t ->
            if (t == null) return@Observer

            if (t.isSuccess) {
                refreshFinished()
                if (t.data == null) {
                    mAdapter.setPayloads(ErrorItem("数据为空"))
                    return@Observer
                }
                if ((t.data as NewsPagination).isFirstPage()) {
                    mAdapter.setPayloads(t.data?.newsList)
                } else {
                    mAdapter.addPayloads(t.data?.newsList)
                }
            } else if (t.isError) {
                refreshFinished()
                mAdapter.setPayloads(ErrorItem("加载失败"))
            } else if (t.isLoading) {

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // If retainInstance is set to be true, on configuration change,
        // View state will be restored,
        // thus the referenced obsolete activity context should not be used any more,
        // otherwise a memory leak may occur.
        binding.recyclerNews.layoutManager = LinearLayoutManager(context)
        binding.recyclerNews.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        mAdapter = RecyclerAdapter()
        mAdapter.setFragmentManager(childFragmentManager)
        mAdapter.register(NewsViewHolder::class.java,
                ErrorViewHolder::class.java)
        binding.recyclerNews.adapter = mAdapter

        //
        val headerParams = binding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        val headerBehavior = RefreshHeaderBehavior<View>(context)
        headerBehavior.addOnPullingListener(object : OnPullListener {

            override fun onStartPulling(max: Int, isTouch: Boolean) {
                binding.headerProgress.visibility = View.VISIBLE
                binding.headerProgress.max = max
            }

            override fun onPulling(current: Int, delta: Int, max: Int, isTouch: Boolean) {
                binding.headerProgress.progress = current
            }

            override fun onStopPulling(current: Int, max: Int) {
                binding.headerProgress.progress = current
            }

        })

        headerBehavior.addOnRefreshListener(object : OnRefreshListener {

            override fun onRefreshStart() {
            }

            override fun onReleaseToRefresh() {
            }

            override fun onRefresh() {
                loadFirstPage()
            }

            override fun onRefreshEnd() {
            }
        })

        headerParams.behavior = headerBehavior
        binding.scrollHeader.layoutParams = headerParams

        // Set footer behavior.
        val footerParams = binding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        val footerBehavior = RefreshFooterBehavior<View>(context)
        footerBehavior.addOnRefreshListener(object : OnRefreshListener {

            override fun onRefreshStart() {
            }

            override fun onReleaseToRefresh() {
            }

            override fun onRefresh() {
                loadNextPage()
            }

            override fun onRefreshEnd() {
            }
        })

        footerBehavior.addOnPullingListener(object : OnPullListener {
            override fun onPulling(current: Int, delta: Int, max: Int, isTouch: Boolean) {
            }

            override fun onStartPulling(max: Int, isTouch: Boolean) {

            }

            override fun onStopPulling(current: Int, max: Int) {
            }
        })

        footerParams.behavior = footerBehavior
        binding.scrollFooter.layoutParams = footerParams

        // Set content behavior.
        val contentParams = binding.recyclerNews.layoutParams as CoordinatorLayout.LayoutParams
        val refreshHeaderBehavior = RefreshContentBehavior<View>(context)
        contentParams.behavior = refreshHeaderBehavior

        refreshHeaderBehavior.addOnPullingListener(object : OnPullListener {
            override fun onStartPulling(max: Int, isTouch: Boolean) {
                binding.contentProgress.visibility = View.VISIBLE
                binding.contentProgress.max = max
            }

            override fun onPulling(current: Int, delta: Int, max: Int, isTouch: Boolean) {
                binding.contentProgress.progress = current
            }

            override fun onStopPulling(current: Int, max: Int) {
                binding.contentProgress.progress = current
            }
        })

        binding.recyclerNews.layoutParams = contentParams

//        // pull up to load more
        binding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            val THRESHOULD = PAGE_SIZE / 2

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy == 0)
                    return
                val lastVisibleItem = (recyclerView?.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                val totalItemCount = recyclerView.layoutManager.itemCount
                if (totalItemCount <= lastVisibleItem + THRESHOULD) {
//                    footerBehavior.refresh()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (null == savedInstanceState || mNewsModel.mNews.value == null) {
            val lpHeader = binding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = lpHeader.behavior as RefreshHeaderBehavior
            behavior.refresh()
        }
    }

    fun refreshFinished() {
        val lpHeader = binding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = lpHeader.behavior as RefreshHeaderBehavior
        behavior.refreshComplete()

        val lpFooter = binding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        val footerBehavior = lpFooter.behavior as RefreshFooterBehavior
        footerBehavior.refreshComplete()
    }

    override fun loadFirstPage() {
        mNewsModel.refreshPage()
    }

    override fun loadNextPage() {
        mNewsModel.nextPage()
    }
}