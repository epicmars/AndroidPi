package cn.androidpi.app.components.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.components.base.RecyclerAdapter
import cn.androidpi.app.components.viewholder.TodoViewHolder
import cn.androidpi.app.databinding.FragmentTodoBinding
import cn.androidpi.app.viewmodel.TodoViewModel
import cn.androidpi.note.entity.Todo
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_todo)
class TodoFragment : BaseFragment<FragmentTodoBinding>() {

    @Inject
    lateinit var mTodoModel: TodoViewModel

    var mAdapter: RecyclerAdapter? = null

    companion object {
        fun newInstance(): TodoFragment {
            return TodoFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = RecyclerAdapter()
        mAdapter?.register(TodoViewHolder::class.java)
        mBinding?.recyclerTodo?.setHasFixedSize(true)
        mBinding?.recyclerTodo?.layoutManager = GridLayoutManager(context, 2)
        mBinding?.recyclerTodo?.adapter = mAdapter

        // todo remove this when dependencies injection is down
        if (null == mTodoModel) {
            val aTestTodo = Todo()
            aTestTodo.content = "Test for todo"
            mAdapter?.setPayloads(List(12, {
                index -> aTestTodo
            }))
        }
        mTodoModel.mTodoToday.observe(this, object : Observer<Array<Todo>> {
            override fun onChanged(t: Array<Todo>?) {
                mAdapter?.setPayloads(t?.toList())
            }
        })
    }
}