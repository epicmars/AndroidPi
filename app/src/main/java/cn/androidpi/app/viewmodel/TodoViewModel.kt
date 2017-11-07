package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.data.repository.TodoRepo
import cn.androidpi.note.entity.Todo
import cn.androidpi.note.model.TodoModel
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */

class  TodoViewModel @Inject constructor() : ViewModel(), TodoModel {

    @Inject
    var mTodoRepo: TodoRepo? = null

    @Inject
    var mTodoToday: MutableLiveData<Array<Todo>>? = null

    override fun addTodoItem(startTime: Date, deadline: Date, whatTodo: String) {
        mTodoRepo?.addTodoItem(startTime, deadline, whatTodo)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : CompletableObserver {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }

    override fun whatTodoToday() {

        mTodoRepo?.todoToday()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : SingleObserver<Array<Todo>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onSuccess(t: Array<Todo>?) {
                        mTodoToday?.value = t
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }
}