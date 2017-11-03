package cn.androidpi.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

import cn.androidpi.data.local.dao.TodoDao
import cn.androidpi.note.entity.Todo

/**
 * Created by jastrelax on 2017/11/2.
 */

@Database(entities = arrayOf(Todo::class), version = 1)
@TypeConverters(DateConverter::class, StringArrayConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
}
