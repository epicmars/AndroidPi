package cn.androidpi.app.ui.viewholder

import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.ViewHolderTextNoteBinding
import cn.androidpi.app.ui.base.BaseViewHolder
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.note.entity.TextNote

/**
 * Created by jastrelax on 2017/12/7.
 */
@BindLayout(value = R.layout.view_holder_text_note, dataTypes = arrayOf(TextNote::class))
class TextNoteViewHolder(itemView: View) : BaseViewHolder<ViewHolderTextNoteBinding>(itemView) {

    override fun <T : Any?> onBindView(data: T, position: Int) {
        if (data == null)
            return
        if (data is TextNote) {
            mBinding.tvContent.text = data.text
        }
    }
}