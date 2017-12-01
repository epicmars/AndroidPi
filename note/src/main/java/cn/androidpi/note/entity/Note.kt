package cn.androidpi.note.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by jastrelax on 2017/11/22.
 */
class Note {

    @PrimaryKey
    var id: Long? = null

    /** 创建时间 **/
    @ColumnInfo(name = "created_time")
    var createdTime: Date? = null

    /**
     * 更新时间
     */
    @ColumnInfo(name = "update_time")
    var updateTime: Date? = null
}