package cn.androidpi.note.entity

import android.arch.persistence.room.ColumnInfo
import java.util.*

/**
 * Created by jastrelax on 2017/11/22.
 */
class Note {

    /** 创建时间 **/
    @ColumnInfo(name = "created_time")
    var createdTime: Date? = null

    /**
     * 更新时间
     */
    @ColumnInfo(name = "update_time")
    var updateTime: Date? = null
}