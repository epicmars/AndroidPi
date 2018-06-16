package com.androidpi.app.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import com.androidpi.app.R
import com.androidpi.app.base.BaseActivity
import com.androidpi.app.base.BindLayout
import com.androidpi.app.buiness.viewmodel.TextNoteEditViewModel
import com.androidpi.app.buiness.viewmodel.ViewModelFactory
import com.androidpi.app.databinding.ActivityTextNoteEditBinding
import javax.inject.Inject

@BindLayout(R.layout.activity_text_note_edit)
class TextNoteEditActivity : BaseActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    lateinit var mTextNoteEditViewModel: TextNoteEditViewModel

    lateinit var mBinding: ActivityTextNoteEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_text_note_edit)
        mTextNoteEditViewModel = mViewModelFactory.create(TextNoteEditViewModel::class.java)

        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mTextNoteEditViewModel.updateText(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        mBinding.btnCommit.setOnClickListener { v ->
            if (mTextNoteEditViewModel.isValid()) {
                mTextNoteEditViewModel.saveTextNote()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Snackbar.make(v, "笔记内容为空", Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}
