package cn.androidpi.data.repository.impl

import cn.androidpi.data.local.dao.TodoDao
import cn.androidpi.note.entity.Todo
import cn.androidpi.note.repo.TodoRepo
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/11/2.
 */
class TodoRepository : TodoRepo {

    @Inject
    val todoDao: TodoDao? = null

    override fun newTodo(startTime: Date, deadline: Date, whatTodo: String): Completable {
        return Completable.fromAction {
            val todo = Todo()
            todo.createdTime = Date()
            todo.startTime = startTime
            todo.deadline = deadline
            todo.content = whatTodo
            todoDao!!.insert(todo)
        }
    }

    override fun todoToday(): Single<Array<Todo>> {
        return Single.fromCallable {
            todoDao!!.todoToday()
        }
    }
}