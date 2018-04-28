package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.app.viewmodel.vo.Resource
import cn.androidpi.note.repo.TextNoteRepo
import cn.androidpi.note.entity.TextNote
import cn.androidpi.note.model.TextNotesModel
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

                    override fun onError(e: Throwable?) {
                        mTextNotes.value = Resource.error("加载笔记失败", null)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onSuccess(t: List<TextNote>?) {
                        if (t == null) {
                            mTextNotes.value = Resource.error("笔记数据出错啦！", null)
                            return
                        }
                        mTextNotes.value = Resource.success(t)
                    }
                })
    }
}