package com.androidpi.note.local.dao

import android.arch.persistence.room.*
import com.androidpi.note.entity.Todo


/**
 * 待办事项Dao，提供增删改查基本功能。
 * Created by jastrelax on 2017/11/2.
 */

@Dao
interface TodoDao {

    @Insert
    fun insert(vararg todoItems: Todo): Array<Long>

    @Query("select * from todo")
    fun findAll(): Array<Todo>

    @Query("select * from todo where id = :id")
    fun findById(id: Long): Todo

    @Query("select * from todo where date(start_time/1000, 'unixepoch') = date('now')")
    fun findTodoToday(): Array<Todo>

    @Update
    fun update(vararg todoItems: Todo)

    @Delete
    fun delete(vararg todoItems: Todo)
}
