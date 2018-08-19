package com.androidpi.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.androidpi.note.local.dao.TodoDao
import com.androidpi.note.entity.Todo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

/**
 * {@link https://github.com/googlesamples/android-architecture-components}
 * Created by jastrelax on 2017/11/2.
 */
@RunWith(AndroidJUnit4::class)
class TestTodoDao {
    var todoDao: TodoDao? = null
    var noteDb: NoteDatabase? = null

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        noteDb = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
                .allowMainThreadQueries().build()
//        noteDb = Room.databaseBuilder(context, NoteDatabase::class.java, "test_note.db")
//                .build()
        // safe call
        todoDao = noteDb!!.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        noteDb!!.close()
    }

    @Test
    fun testTodoDao() {

        // 无论是内存中还是本地存储中的数据库，先删除所有数据
        var todoItems = todoDao!!.findAll()
        todoDao!!.delete(*todoItems)
        todoItems = todoDao!!.findAll()
        assertEquals(0, todoItems.size)

        // 插入一条今日待办事项
        val todo = createTodo()
        todoDao!!.insert(todo)

        // 获取所有事项
        todoItems = todoDao!!.findAll()
        assertEquals(1, todoItems.size)

        // 获取今天的事项
        var todoItemsToday = todoDao!!.findTodoToday()
        assertEquals(1, todoItemsToday.size)
        for (t in todoItemsToday) {
            Log.d("what to do today:",  t.content)
        }

        val todoToday = todoItemsToday[0]
        assertEquals(todo.createdTime, todoToday.createdTime)
        assertEquals(todo.startTime, todoToday.startTime)

        // 更新今天的事项
        todoToday.content = "吃火锅后看电影"
        todoDao!!.update(todoToday)
        for (t in todoDao!!.findTodoToday()) {
            Log.d("what to do today:",  t.content)
        }
    }

    @Test
    fun testFindById() {
        val newTodo = createTodo()
        val ids = todoDao?.insert(newTodo)
        val todo = todoDao?.findById(ids!![0])
        assertEquals(newTodo, todo)
    }

    private fun createTodo(): Todo {
        val todo = Todo()
        todo.createdTime = Date()
        todo.content = "吃火锅"
        todo.startTime = Date()
        todo.tags = arrayOf("美食")
        todo.status = Todo.Status.START
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_WEEK, 1)
        todo.deadline = cal.time
        return todo
    }
}