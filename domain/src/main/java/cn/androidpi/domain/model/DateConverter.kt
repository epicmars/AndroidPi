package cn.androidpi.domain.model

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * room数据类型转换，日期到Unix时间戳(毫秒)的转换。
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