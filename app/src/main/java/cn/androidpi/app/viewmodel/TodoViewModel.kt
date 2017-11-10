package cn.androidpi.app.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import cn.androidpi.data.repository.TodoRepo
import cn.androidpi.note.entity.Todo
import cn.androidpi.note.model.TodoModel
import dagger.Lazy
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */

class TodoViewModel @Inject constructor() : ViewModel(), TodoModel {

    @Inject
    lateinit var mTodoRepo: Lazy<TodoRepo>

    val mTodoToday: MutableLiveData<Array<Todo>> = MutableLiveData()
    val mTodoList: MutableLiveData<Array<Todo>> = MutableLiveData()

    override fun loadTodoList() {
        mTodoRepo.get().todoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Array<Todo>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onSuccess(t: Array<Todo>?) {
                        mTodoList.value = t
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }

    override fun loadTodoToday() {
        mTodoRepo.get().todoToday()
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(object : SingleObserver<Array<Todo>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onSuccess(t: Array<Todo>?) {
                        mTodoToday.value = t
                    }

                    override fun onError(e: Throwable?) {
                    }
                })
    }
}