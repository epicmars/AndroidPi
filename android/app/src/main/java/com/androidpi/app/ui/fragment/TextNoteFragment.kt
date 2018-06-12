package com.androidpi.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.androidpi.app.R
import com.androidpi.app.databinding.FragmentTextNoteBinding
import com.androidpi.app.ui.base.BaseFragment
import com.androidpi.app.ui.base.BindLayout
import com.androidpi.note.entity.TextNote

/**
 * Created by jastrelax on 2017/12/29.
 */
@BindLayout(R.layout.fragment_text_note, injectable = false)
class TextNoteFragment : BaseFragment<FragmentTextNoteBinding>() {

    companion object {
        val ARGS_TEXT_NOTE = "TextNoteFragment.ARGS_TEXT_NOTE"
        fun newInstance(textNote: TextNote): TextNoteFragment {
            val args = Bundle()
            args.putParcelable(ARGS_TEXT_NOTE, textNote)
            val fragment = TextNoteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textNote: TextNote? = arguments?.getParcelable(ARGS_TEXT_NOTE)
        if (textNote == null)
            return
        mBinding.tvText.text = textNote.text
    }
}