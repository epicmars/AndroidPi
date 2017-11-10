package cn.androidpi.app.view

import java.util.*

/**
 * Created by jastrelax on 2017/11/10.
 */
interface TodoEditView {

    fun updateStartTime(startTime: Date)

    fun updateDeadline(deadline: Date)

    fun updateTodoContent(content: String)

    fun commitTodoItem()
}