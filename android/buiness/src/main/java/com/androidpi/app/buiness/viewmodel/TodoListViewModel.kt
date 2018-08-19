package com.androidpi.app.buiness.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.app.base.vm.vo.Resource
import com.androidpi.note.repo.TodoRepo
import com.androidpi.note.entity.Todo
import com.androidpi.note.model.TodoModel
import dagger.Lazy
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/7.
 */

class TodoListViewModel @Inject constructor() : ViewModel(), TodoModel {

    @Inject
    lateinit var mTodoRepo: Lazy<TodoRepo>

    val mTodoToday: MutableLiveData<Array<Todo>> = MutableLiveData()
    val mTodoList: MutableLiveData<Resource<Array<Todo>>> = MutableLiveData()

    override fun loadTodoList() {
        mTodoRepo.get().todoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Array<Todo>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: Array<Todo>) {
                        if (t.isEmpty()) {
                            mTodoList.value = Resource.error("待办事项为空")
                        } else {
                            mTodoList.value = Resource.success(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        mTodoList.value = Resource.error("加载待办事项错误")
                    }
                })
    }

    override fun loadTodoToday() {
        mTodoRepo.get().todoToday()
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(object : SingleObserver<Array<Todo>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: Array<Todo>) {
                        mTodoToday.value = t
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }
}