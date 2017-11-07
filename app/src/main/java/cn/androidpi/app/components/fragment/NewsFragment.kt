package cn.androidpi.app.components.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.components.base.RecyclerAdapter
import cn.androidpi.app.components.viewholder.NewsViewHolder
import cn.androidpi.app.databinding.FragmentNewsBinding
import cn.androidpi.app.viewmodel.NewsViewModel
import cn.androidpi.news.entity.News
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_news)
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    @Inject
    var mNewsModel: NewsViewModel? = null

    var mAdapter: RecyclerAdapter? = null

    companion object {
        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = RecyclerAdapter()
        mAdapter?.register(NewsViewHolder::class.java)
        mBinding.recyclerNews.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerNews.adapter = mAdapter

        // todo remove this when dependencies injection is down
        if (null == mNewsModel) {
            val aTestNews = News()
            aTestNews.title = "A test News."
            mAdapter?.setPayloads(List(12) {
                index ->  aTestNews
            })
        }

        mNewsModel?.mNews?.observe(this, object : Observer<List<News>> {
            override fun onChanged(t: List<News>?) {
                mAdapter?.setPayloads(t)
            }
        })
    }

}