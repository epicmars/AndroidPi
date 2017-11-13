package cn.androidpi.data.local

import android.arch.persistence.room.TypeConverter

/**
 * 字符串数组到字符串间的转换，转换后的字符数组项使用","号分隔。
 * Created by jastrelax on 2017/11/2.
 */
class StringArrayConverter {

    @TypeConverter
    fun fromArray(array: Array<String>?): String? {
        val sb = StringBuilder()
        if (array == null)
            return null
        for (s in array) {
            sb.append(s)
            sb.append(",")
        }
        return if (sb.lastIndex < 0) null else sb.deleteCharAt(sb.lastIndex).toString()
    }

    @TypeConverter
    fun toArray(string: String?): Array<String>?{
        return string?.split(",")?.toTypedArray()
    }
}