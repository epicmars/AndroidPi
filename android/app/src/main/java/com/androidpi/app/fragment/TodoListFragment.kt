package com.androidpi.app.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidpi.app.R
import com.androidpi.app.activity.TodoEditActivity
import com.androidpi.app.base.di.Injectable
import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.base.ui.RecyclerAdapter
import com.androidpi.app.buiness.view.TodoView
import com.androidpi.app.buiness.viewmodel.TodoListViewModel
import com.androidpi.app.base.vm.vo.Resource
import com.androidpi.app.databinding.FragmentTodoListBinding
import com.androidpi.app.viewholder.ErrorViewHolder
import com.androidpi.app.viewholder.TodoViewHolder
import com.androidpi.app.viewholder.items.ErrorItem
import com.androidpi.note.entity.Todo
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(R.layout.fragment_todo_list)
@Injectable
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
        mAdapter?.register(TodoViewHolder::class.java,
                ErrorViewHolder::class.java)
        binding.recyclerTodo.setHasFixedSize(true)
        binding.recyclerTodo.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerTodo.adapter = mAdapter

        binding.btnAdd.setOnClickListener {
            startActivityForResult(Intent(context, TodoEditActivity::class.java), REQUEST_ADD_TODO_ITEM)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTodoModel.mTodoList.observe(this, object : Observer<Resource<Array<Todo>>> {
            override fun onChanged(t: Resource<Array<Todo>>?) {
                if (t == null || t.isError()) {
                    val errorItem = ErrorItem("oops,出错了!")
                    if (t != null) errorItem.message = t.message
                    (binding.recyclerTodo.layoutManager as GridLayoutManager).spanCount = 1
                    mAdapter?.setPayloads(errorItem)
                } else if (t.isSuccess()) {
                    (binding.recyclerTodo.layoutManager as GridLayoutManager).spanCount = 2
                    mAdapter?.setPayloads(t.data?.toList())
                }
            }
        })
        if (null == savedInstanceState || mTodoModel.mTodoList.value == null) {
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