package com.androidpi.note.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * 一个Todo事项。
 * Created by jastrelax on 2017/11/1.
 */

@Entity(tableName = "todo")
class Todo() : Parcelable {

    enum class Status {
        /** 新建 **/
        NEW,              // startTime > now
        /** 开始 **/
        START,            // startTime <= now <= deadline
        /** 完成 **/
        FINISH,           // user action
        /** 取消 **/
        CANCEL,           // user action
        /** 逾期 **/
        OVERDUE,           // deadline < now and status == START
        /** 删除 **/       // deleted by user
        DELETED
    }

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

    /**
     * 标签
     */
    var tags: Array<String>? = null

    /**
     * 状态
     */
    var status: Status? = Status.NEW

    /**
     * 优先级，由低到高，数值为[0,100]，0或null表示无优先级。
     */
    var priority: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Todo

        if (createdTime != other.createdTime) return false
        if (updateTime != other.updateTime) return false
        if (startTime != other.startTime) return false
        if (deadline != other.deadline) return false
        if (content != other.content) return false
        if (!Arrays.equals(tags, other.tags)) return false
        if (status != other.status) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = createdTime?.hashCode() ?: 0
        result = 31 * result + (updateTime?.hashCode() ?: 0)
        result = 31 * result + (startTime?.hashCode() ?: 0)
        result = 31 * result + (deadline?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (tags?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (priority?.hashCode() ?: 0)
        return result
    }

    constructor(source: Parcel) : this() {
        id = source.readLong()
        createdTime = Date(source.readLong())
        updateTime = Date(source.readLong())
        startTime = Date(source.readLong())
        deadline = Date(source.readLong())
        content = source.readString()
        tags = source.createStringArray()
        val stat = source.readInt()
        status = if(stat != -1) Status.values()[stat] else null
        val prio = source.readInt()
        priority = if (prio != -1) prio else null
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        dest.writeLong(id ?: -1L)
        dest.writeLong(createdTime?.time ?: 0L)
        dest.writeLong(updateTime?.time ?: 0L)
        dest.writeLong(startTime?.time ?: 0L)
        dest.writeLong(deadline?.time ?: 0L)
        dest.writeString(content)
        dest.writeStringArray(tags)
        dest.writeInt(status?.ordinal ?: -1)
        dest.writeInt(priority ?: -1)
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Todo> = object : Parcelable.Creator<Todo> {
            override fun createFromParcel(source: Parcel): Todo = Todo(source)
            override fun newArray(size: Int): Array<Todo?> = arrayOfNulls(size)
        }
    }
}
