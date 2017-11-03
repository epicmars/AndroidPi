package cn.androidpi.note.repo

import cn.androidpi.note.entity.Todo
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

/**
 * Created by jastrelax on 2017/11/2.
 */
interface TodoRepo {

    fun newTodo(startTime: Date, deadline: Date, whatTodo: String): Completable

    fun todoToday(): Single<Array<Todo>>

}