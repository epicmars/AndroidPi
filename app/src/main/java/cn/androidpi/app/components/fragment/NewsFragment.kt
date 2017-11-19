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
import cn.androidpi.app.viewmodel.NewsViewModel
import cn.androidpi.app.widget.PullDownHeaderBehavior
import cn.androidpi.app.widget.PullUpFooterBehavior
import cn.androidpi.app.widget.PullingListener
import cn.androidpi.app.widget.ScrollViewBehavior
import java.lang.Exception
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
        val behavior = PullDownHeaderBehavior(context)
        behavior.addPullDownListener(object : PullingListener {
            override fun onRefreshCancelled() {

            }

            override fun onRefreshFinish() {

            }

            override fun onRefreshException(exception: Exception?) {

            }

            override fun onRefreshTimeout() {

            }

            override fun onRefresh() {
                refreshWithSwipe()
            }
        })
        lpHeader.behavior = behavior
        mBinding.scrollHeader.layoutParams = lpHeader

        val lpFooter = mBinding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        val footerBehavior = PullUpFooterBehavior<View>(context)
        footerBehavior.addPullUpListener(object : PullingListener {
            override fun onRefreshCancelled() {

            }

            override fun onRefreshFinish() {
            }

            override fun onRefreshException(exception: Exception?) {
            }

            override fun onRefreshTimeout() {
            }

            override fun onRefresh() {
                loadNextPage()
            }
        })

        lpFooter.behavior = footerBehavior;
        mBinding.scrollFooter.layoutParams = lpFooter

        val lpScroll = mBinding.recyclerNews.layoutParams as CoordinatorLayout.LayoutParams
        lpScroll.behavior = ScrollViewBehavior<View>()
        mBinding.recyclerNews.layoutParams = lpScroll
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNewsModel.mNews.observe(this, Observer { t ->
            refreshFinished()
            mAdapter.setPayloads(t)
        })
        if (null == savedInstanceState) {
            refreshWithSwipe()
        }
    }

    fun refreshWithSwipe() {
        loadFirstPage()
//        mBinding.swipeRefresh.isRefreshing = true
    }

    fun refreshFinished() {
//        mBinding.swipeRefresh.isRefreshing = false
    }

    override fun loadFirstPage() {
        mNewsModel.refreshPage()
    }

    override fun loadNextPage() {
        mNewsModel.nextPage()
    }
}