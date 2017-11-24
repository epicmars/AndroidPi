package cn.androidpi.app.components.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseFragment
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.databinding.FragmentTodoBinding
import cn.androidpi.note.entity.Todo

/**
 * Created by jastrelax on 2017-11-14
 */
@BindLayout(R.layout.fragment_todo)
class TodoFragment : BaseFragment<FragmentTodoBinding>() {

    private var mTodo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTodo = arguments!!.getParcelable(ARG_TODO_ITEM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvTodoContent.text = mTodo?.content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.supportStartPostponedEnterTransition()
        }
    }

    companion object {
        private val ARG_TODO_ITEM = "TodoFragment.ARG_TODO_ITEM"

        fun newInstance(todo: Todo): TodoFragment {
            val fragment = TodoFragment()
            val args = Bundle()
            args.putParcelable(ARG_TODO_ITEM, todo)
            fragment.arguments = args
            return fragment
        }
    }
}
