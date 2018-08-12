package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.app.buiness.viewmodel.vo.Resource
import com.androidpi.note.repo.TextNoteRepo
import com.androidpi.note.entity.TextNote
import com.androidpi.note.model.TextNotesModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/12/7.
 */
class NotesViewModel @Inject constructor(): ViewModel(), TextNotesModel {

    @Inject
    lateinit var mTextNotesRepo: TextNoteRepo

    var mTextNotes = MutableLiveData<Resource<List<TextNote>>>()

    override fun getAllTextNotes() {
        mTextNotesRepo.getAllTextNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<TextNote>> {

                    override fun onError(e: Throwable) {
                        mTextNotes.value = Resource.error("加载笔记失败")
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: List<TextNote>) {
                        mTextNotes.value = Resource.success(t)
                    }
                })
    }
}