package cn.androidpi.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import cn.androidpi.data.local.NoteDatabase
import cn.androidpi.data.local.dao.TodoDao
import cn.androidpi.note.entity.Todo
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
//        noteDb = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
//                .allowMainThreadQueries().build()
        noteDb = Room.databaseBuilder(context, NoteDatabase::class.java, "test_note.db")
                .build()
        // safe call
        todoDao = noteDb!!.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        noteDb!!.close()
    }

    @Test
    fun addAndReadTodo() {

        // 无论是内存中还是本地存储中的数据库，先删除所有数据
        var todoItems = todoDao!!.getAll()
        todoDao!!.delete(*todoItems)
        todoItems = todoDao!!.getAll()
        assertEquals(0, todoItems.size)

        // 插入一条今日待办事项
        val todo = Todo()
        todo.createdTime = Date()
        todo.content = "吃火锅"
        todo.startTime = Date()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_WEEK, 1)
        todo.deadline = cal.time
        todoDao!!.insert(todo)

        // 获取所有事项
        todoItems = todoDao!!.getAll()
        assertEquals(1, todoItems.size)

        // 获取今天的事项
        var todoItemsToday = todoDao!!.todoToday()
        assertEquals(1, todoItemsToday.size)
        for (t in todoItemsToday) {
            Log.d("what to do today:",  t.content)
        }

        val todoToday = todoItemsToday[0]
        // 更新今天的事项
        todoToday.content = "吃火锅后看电影"
        todoDao!!.update(todoToday)
        for (t in todoDao!!.todoToday()) {
            Log.d("what to do today:",  t.content)
        }

    }
}