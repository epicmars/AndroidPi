package cn.androidpi.note.model

import java.util.*

/**
 * Created by jastrelax on 2017/11/7.
 */
interface TodoModel {

    fun addTodoItem(startTime: Date, deadline: Date, whatTodo: String)

    fun whatTodoToday()
}