package cn.androidpi.app.ui.viewholder

import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.view.ViewGroup
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ViewHolderTodoBinding
import cn.androidpi.app.ui.activity.TemplateActivity
import cn.androidpi.app.ui.base.BaseViewHolder
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.app.ui.fragment.FragmentFactory
import cn.androidpi.app.ui.fragment.TodoFragment
import cn.androidpi.app.viewmodel.TodoEditViewModel
import cn.androidpi.common.datetime.DateTimeUtils
import cn.androidpi.note.entity.Todo
import java.util.*

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(value = R.layout.view_holder_todo, dataTypes = arrayOf(Todo::class))
class TodoViewHolder(itemView: View) : BaseViewHolder<ViewHolderTodoBinding>(itemView) {

    override fun <T : Any?> onBindView(data: T, position: Int) {
        val todo = data as? Todo
        mBinding?.priorityColor?.setBackgroundColor(TodoEditViewModel.priorityColor(todo?.priority))
        mBinding?.tvTodoContent?.text = todo?.content
        mBinding?.tvCreatedTime?.text = getElapsedDays(todo?.createdTime)
        itemView.setOnClickListener {
            val sharedElementName = it.resources.getString(R.string.transition_todo_content)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, itemView, sharedElementName)
            TemplateActivity.startWith(options, it.context, fragmentName = TodoFragment::class.java.canonicalName,
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
        val clp = itemView.layoutParams as ViewGroup.MarginLayoutParams
        clp.width = sw / 2
        clp.height = clp.width
        itemView.layoutParams = clp
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        itemView.setOnClickListener(null)
    }

    fun getElapsedDays(createdTime: Date?): String? {
        if (createdTime == null)
            return null
        val days = DateTimeUtils.elapsedDaysFromNow(createdTime)
        if (days == 0) {
            return itemView.context.getString(R.string.created_at, "今天")
        } else if (days < 0) {
            return itemView.context.getString(R.string.created_after, String.format("%d天", -days))
        } else {
            return itemView.context.getString(R.string.created_before, String.format("%d天", days))
        }
    }
}
