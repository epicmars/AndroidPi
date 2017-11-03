package cn.androidpi.note.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * 一个Todo事项。
 * Created by jastrelax on 2017/11/1.
 */

@Entity(tableName = "todo")
class Todo {
    @PrimaryKey
    var id: Long? = null

    /** 创建时间 **/
    @ColumnInfo(name = "created_time")
    var createdTime: Date? = null

    /**
     * 开始时间
     */
    @ColumnInfo(name = "start_time")
    var startTime: Date? = null

    /**
     * 截止时间
     */
    var deadline: Date? = null

    /**
     * 内容
     */
    var content: String? = null

}
