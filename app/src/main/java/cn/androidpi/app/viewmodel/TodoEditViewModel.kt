package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.data.repository.TodoRepo
import cn.androidpi.note.model.TodoEditModel
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/10.
 */
class TodoEditViewModel @Inject constructor(): ViewModel(), TodoEditModel {

    @Inject
    lateinit var mTodoRepo: TodoRepo

    var mStartTime: MutableLiveData<Date> = MutableLiveData()
    var mDeadline: MutableLiveData<Date> = MutableLiveData()
    var mTodoContent: MutableLiveData<String> = MutableLiveData()

    override fun addTodoItem() {
        try {
            mTodoRepo.saveTodoItem(mStartTime.value!!, mDeadline.value!!, mTodoContent.value!!)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        } catch (e: NullPointerException){
            Timber.e(e)
        }
    }

    fun isValidContent(): Boolean {
        return !mTodoContent.value.isNullOrBlank()
    }

    fun isValidDate(): Boolean {
        if(mStartTime.value == null || mDeadline.value == null) {
            return false
        }
        return mDeadline.value!! == (mStartTime.value) || mDeadline.value!!.after(mStartTime.value)
    }
}