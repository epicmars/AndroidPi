package cn.androidpi.app.components.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.activity.TodoEditActivity
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

    companion object {
        val REQUEST_ADD_TODO_ITEM = 100

        fun newInstance(): TodoFragment {
            return TodoFragment()
        }
    }

    @Inject
    lateinit var mTodoModel: TodoViewModel

    var mAdapter: RecyclerAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = RecyclerAdapter()
        mAdapter?.register(TodoViewHolder::class.java)
        mBinding?.recyclerTodo?.setHasFixedSize(true)
        mBinding?.recyclerTodo?.layoutManager = GridLayoutManager(context, 2)
        mBinding?.recyclerTodo?.adapter = mAdapter

        mBinding.btnAdd.setOnClickListener {
            startActivityForResult(Intent(context, TodoEditActivity::class.java), REQUEST_ADD_TODO_ITEM)
        }

        mTodoModel.mTodoToday.observe(this, object : Observer<Array<Todo>> {
            override fun onChanged(t: Array<Todo>?) {
                mAdapter?.setPayloads(t?.toList())
            }
        })

        mTodoModel.whatTodoToday()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_TODO_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                mTodoModel.whatTodoToday()
            }
        }
    }
}