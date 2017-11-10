package cn.androidpi.app.components.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
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
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_news)
class NewsFragment : BaseFragment<FragmentNewsBinding>(), NewsView {

    @Inject
    lateinit var mNewsModel: NewsViewModel

    val mAdapter: RecyclerAdapter = RecyclerAdapter()

    companion object {
        fun newInstance(): NewsFragment {
            val newsFragment = NewsFragment()
            newsFragment.retainInstance = true
            return newsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null == savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState)

            mBinding.recyclerNews.layoutManager = LinearLayoutManager(context)
            mBinding.recyclerNews.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            mBinding.recyclerNews.adapter = mAdapter
            // pull down to refresh
            mBinding.swipeRefresh.setOnRefreshListener({
                loadFirstPage()
            })

            // pull up to load more
            mBinding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItem = (recyclerView?.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val totalItemCount = recyclerView?.layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - 6) {
                        loadNextPage()
                    }
                }
            })
        }
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (null == savedInstanceState) {
            refreshWithSwipe()
            mAdapter.register(NewsViewHolder::class.java)
            mNewsModel.mNews.observe(this, Observer { t ->
                refreshFinished()
                mAdapter.setPayloads(t)
            })
        }
    }

    fun refreshWithSwipe() {
        loadFirstPage()
        mBinding.swipeRefresh.isRefreshing = true
    }

    fun refreshFinished() {
        mBinding.swipeRefresh.isRefreshing = false
    }

    override fun loadFirstPage() {
        mNewsModel.refreshPage()
    }

    override fun loadNextPage() {
        mNewsModel.nextPage()
    }
}