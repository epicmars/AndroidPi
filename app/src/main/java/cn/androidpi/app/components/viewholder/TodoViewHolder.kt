package cn.androidpi.app.components.viewholder

import android.view.View
import android.view.ViewGroup
import cn.androidpi.app.R
import cn.androidpi.app.components.activity.TemplateActivity
import cn.androidpi.app.components.base.BaseViewHolder
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.components.fragment.FragmentFactory
import cn.androidpi.app.components.fragment.TodoFragment
import cn.androidpi.app.databinding.ViewHolderTodoBinding
import cn.androidpi.note.entity.Todo

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(value = R.layout.view_holder_todo, dataTypes = arrayOf(Todo::class))
class TodoViewHolder(itemView: View) : BaseViewHolder<ViewHolderTodoBinding>(itemView) {

    override fun <T : Any?> onBindView(data: T) {
        val todo = data as? Todo
        mBinding?.tvTodoContent?.text = todo?.content
        itemView.setOnClickListener {
            TemplateActivity.startWith(it.context, fragmentName = TodoFragment::class.java.canonicalName,
                    factory = object : FragmentFactory<TodoFragment>() {
                        override fun create(): TodoFragment {
                            return TodoFragment.newInstance(todo!!)
                        }
                    })
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // make height equal to width
        val sw = itemView.resources.displayMetrics.widthPixels
        val lp = itemView.layoutParams as ViewGroup.MarginLayoutParams
        lp.width = sw / 2 - lp.marginStart * 2
        lp.height = lp.width
        itemView.layoutParams = lp
    }
}