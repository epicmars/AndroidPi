package cn.androidpi.app.components.viewholder

import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseViewHolder
import cn.androidpi.app.components.base.BindLayout
import cn.androidpi.app.databinding.ViewHolderTodoBinding
import cn.androidpi.note.entity.Todo

/**
 * Created by jastrelax on 2017/11/7.
 */
@BindLayout(value = R.layout.view_holder_todo, dataTypes = arrayOf(Todo::class))
class TodoViewHolder(itemView: View) : BaseViewHolder<ViewHolderTodoBinding>(itemView) {

    override fun <T : Any?> present(data: T) {
        val todo = data as? Todo
        mBinding?.tvTodoContent?.text = todo?.content
    }
}