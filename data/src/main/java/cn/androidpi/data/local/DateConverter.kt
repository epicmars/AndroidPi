package cn.androidpi.data.local

import android.arch.persistence.room.TypeConverter

import java.util.Date

/**
 * room数据类型转换。
 * Created by jastrelax on 2017/6/25.
 */

class DateConverter {

    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Date? {
        return if (null == timestamp) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }


}