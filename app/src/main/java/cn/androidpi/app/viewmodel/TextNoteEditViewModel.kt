package cn.androidpi.app.viewmodel

import android.arch.lifecycle.ViewModel
import cn.androidpi.data.repository.TextNoteRepo
import cn.androidpi.note.entity.TextNote
import cn.androidpi.note.model.TextNoteEditModel
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/12/7.
 */
class TextNoteEditViewModel @Inject constructor() : ViewModel(), TextNoteEditModel {

    @Inject
    lateinit var mTextNoteRepo: TextNoteRepo

    var mText: String? = null
    var mTextNote: TextNote? = null

    override fun saveTextNote() {
        if (mText != null) {
            if (mText!!.isBlank())
                return
            saveNewTextNote()
        } else if (mTextNote != null) {
            updateCurrentTextNote()
        }
    }

    fun saveNewTextNote() {
        mTextNoteRepo.addTextNote(mText!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }

    fun updateCurrentTextNote() {

    }
}