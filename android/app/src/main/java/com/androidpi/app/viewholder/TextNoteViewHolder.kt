package com.androidpi.app.viewholder

import android.view.View
import com.androidpi.app.R
import com.androidpi.app.activity.TemplateActivity
import com.androidpi.app.base.ui.BaseViewHolder
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.ViewHolderTextNoteBinding
import com.androidpi.app.fragment.FragmentFactory
import com.androidpi.app.fragment.TextNoteFragment
import com.androidpi.note.entity.TextNote

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
            itemView.setOnClickListener {
                TemplateActivity.startWith(it.context, fragmentName = TextNoteFragment::class.java.canonicalName,
                        factory = object : FragmentFactory<TextNoteFragment>() {
                            override fun create(): TextNoteFragment {
                                return TextNoteFragment.newInstance(data)
                            }
                        })
            }
        }
    }
}