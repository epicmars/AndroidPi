package cn.androidpi.app.ui.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.androidpi.app.R
import cn.androidpi.app.ui.activity.TodoEditActivity
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.app.ui.base.RecyclerAdapter
import cn.androidpi.app.ui.viewholder.TodoViewHolder
import cn.androidpi.app.databinding.FragmentTodoListBinding
import cn.androidpi.app.view.TodoView
import cn.androidpi.app.viewmodel.TodoListViewModel
import cn.androidpi.note.entity.Todo
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_todo_list)
class TodoListFragment : BaseFragment<FragmentTodoListBinding>(), TodoView {

    companion object {
        val REQUEST_ADD_TODO_ITEM = 100

        fun newInstance(): TodoListFragment {
            val todoFragment = TodoListFragment()
            todoFragment.retainInstance = true
            return todoFragment
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    lateinit var mTodoModel: TodoListViewModel

    var mAdapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTodoModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(TodoListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mAdapter = RecyclerAdapter()
        mAdapter?.register(TodoViewHolder::class.java)
        mBinding.recyclerTodo.setHasFixedSize(true)
        mBinding.recyclerTodo.layoutManager = GridLayoutManager(context, 2)
        mBinding.recyclerTodo.adapter = mAdapter

        mBinding.btnAdd.setOnClickListener {
            startActivityForResult(Intent(context, TodoEditActivity::class.java), REQUEST_ADD_TODO_ITEM)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTodoModel.mTodoList.observe(this, object : Observer<Array<Todo>> {
            override fun onChanged(t: Array<Todo>?) {
                mAdapter?.setPayloads(t?.toList())
            }
        })
        if (null == savedInstanceState) {
            showTodoList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_TODO_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                showTodoList()
            }
        }
    }

    override fun showTodoList() {
        mTodoModel.loadTodoList()
    }

    override fun showTodoToday() {
        mTodoModel.loadTodoToday()
    }
}