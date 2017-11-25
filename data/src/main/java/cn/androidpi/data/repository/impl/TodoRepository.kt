package cn.androidpi.data.repository.impl

import cn.androidpi.data.local.dao.TodoDao
import cn.androidpi.data.repository.TodoRepo
import cn.androidpi.note.entity.Todo
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by jastrelax on 2017/11/2.
 */
@Singleton
class TodoRepository @Inject constructor() : TodoRepo {

    @Inject
    lateinit var todoDao: TodoDao

    override fun todoList(): Single<Array<Todo>> {
        return Single.fromCallable {
            todoDao.findAll()
        }
    }

    override fun saveTodoItem(todo: Todo): Completable {
        return Completable.fromAction {
            todoDao.insert(todo)
        }
    }

    override fun saveTodoItem(startTime: Date?, deadline: Date?, whatTodo: String): Completable {
        return Completable.fromAction {
            val todo = Todo()
            todo.createdTime = Date()
            todo.startTime = startTime
            todo.deadline = deadline
            todo.content = whatTodo
            todoDao.insert(todo)
        }
    }

    override fun todoToday(): Single<Array<Todo>> {
        return Single.fromCallable {
            todoDao.findTodoToday()
        }
    }

    override fun getTodo(id: Long): Single<Todo> {
        return Single.fromCallable {
            todoDao.findById(id)
        }
    }

    override fun updateTodo(todo: Todo): Completable {
        return Completable.fromAction {
            todoDao.update(todo)
        }
    }
}