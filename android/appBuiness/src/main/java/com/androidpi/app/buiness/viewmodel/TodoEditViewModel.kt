package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.common.color.ColorModel
import com.androidpi.common.color.HSV
import com.androidpi.common.datetime.DateTimeUtils
import com.androidpi.note.repo.TodoRepo
import com.androidpi.note.entity.Todo
import com.androidpi.note.model.TodoEditModel
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/10.
 */
class TodoEditViewModel @Inject constructor() : ViewModel(), TodoEditModel {

    companion object {
        val hsv = HSV()
        fun priorityColor(priority: Int?): Int {
            return ColorModel.hsvToRgb(hsv.set(180 - ((priority ?: 0) * 1.8f).toInt(), 0.89f, 1f), alpha = 255)
        }
    }

    @Inject
    lateinit var mTodoRepo: TodoRepo

    var mStartDate = MutableLiveData<Date>()
    var mStartTime = MutableLiveData<Date>()
    var mDeadlineDate = MutableLiveData<Date>()
    var mDeadlineTime = MutableLiveData<Date>()
    //
    var mDateTimeStart = MutableLiveData<Date>()
    var mDateTimeDeadline = MutableLiveData<Date>()
    var mTodoContent = MutableLiveData<String>()
    var mPriority = MutableLiveData<Int>()

    init {
        val now = DateTimeUtils.now()
        mStartDate.value = now.time
        mStartTime.value = now.time
//        mDateTimeStart.value = now.time

        mDeadlineDate.value = now.time
        mDeadlineTime.value = now.time
//        mDateTimeDeadline.value = now.time
        mPriority.value = 0
    }

    override fun addTodoItem() {
        val todo = Todo()
        todo.createdTime = Date()
        todo.startTime = mDateTimeStart.value
        todo.deadline = mDateTimeDeadline.value
        todo.content = mTodoContent.value
        todo.priority = mPriority.value
        try {
            mTodoRepo.saveTodoItem(todo)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        } catch (e: NullPointerException) {
            Timber.e(e)
        }
    }

    fun isValidContent(): Boolean {
        return !mTodoContent.value.isNullOrBlank()
    }

    fun isValidDate(): Boolean {
        if (mDateTimeStart.value == null || mDateTimeDeadline.value == null) {
            return true
        }
        return mDateTimeDeadline.value!! == (mDateTimeStart.value) || mDateTimeDeadline.value!!.after(mDateTimeStart.value)
    }

    fun updateDatetime() {

        val now = Date()
        mDateTimeStart.value = DateTimeUtils.assembleDateTime(mStartDate.value ?: now, mStartTime.value ?: now)
        mDateTimeDeadline.value = DateTimeUtils.assembleDateTime(mDeadlineDate.value ?: now, mDeadlineTime.value ?: now)

        val calendar = Calendar.getInstance()
        calendar.time = mDateTimeStart.value
        calendar.set(Calendar.SECOND, 0)
        mDateTimeStart.value = calendar.time

        calendar.time = mDateTimeDeadline.value
        calendar.set(Calendar.SECOND, 0)
        mDateTimeDeadline.value = calendar.time
    }

    fun updateStartDate(startDate: Date) {
        mStartDate.value = startDate
        updateDatetime()
    }

    fun updateStartTime(startTime: Date) {
        mStartTime.value = startTime
        updateDatetime()
    }

    fun updateDeadlineDate(deadline: Date) {
        mDeadlineDate.value = deadline
        updateDatetime()
    }

    fun updateDeadlineTime(deadlineTime: Date) {
        mDeadlineTime.value = deadlineTime
        updateDatetime()
    }

    fun updatePriority(priority: Int) {
        mPriority.value = priority
    }
}