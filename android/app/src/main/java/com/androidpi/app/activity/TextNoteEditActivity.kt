package com.androidpi.app.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.androidpi.app.R
import com.androidpi.app.base.di.Injectable
import com.androidpi.app.base.ui.BaseActivity
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.buiness.viewmodel.TextNoteEditViewModel
import com.androidpi.app.buiness.viewmodel.ViewModelFactory
import com.androidpi.app.databinding.ActivityTextNoteEditBinding
import javax.inject.Inject

@BindLayout(R.layout.activity_text_note_edit)
@Injectable
class TextNoteEditActivity : BaseActivity<ActivityTextNoteEditBinding>() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    lateinit var mTextNoteEditViewModel: TextNoteEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTextNoteEditViewModel = mViewModelFactory.create(TextNoteEditViewModel::class.java)

        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mTextNoteEditViewModel.updateText(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.btnCommit.setOnClickListener { v ->
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
