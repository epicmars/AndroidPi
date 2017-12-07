package cn.androidpi.app.ui.activity

import android.os.Bundle
import cn.androidpi.app.R
import cn.androidpi.app.ui.base.BaseActivity
import cn.androidpi.app.ui.base.BindLayout

@BindLayout(R.layout.activity_text_note_edit)
class TextNoteEditActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_note_edit)
    }
}
