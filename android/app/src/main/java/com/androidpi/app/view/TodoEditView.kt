package com.androidpi.app.view

import java.util.*

/**
 * Created by jastrelax on 2017/11/10.
 */
interface TodoEditView {

    fun updateStartDate(startDate: Date)

    fun updateStartTime(startTime: Date)

    fun updateDeadlineDate(deadlineDate: Date)

    fun updateDeadlineTime(deadlineTime: Date)

    fun updateTodoPriority(priority: Int)

    fun updateTodoContent(content: String)

    fun commitTodoItem()
}