package cn.androidpi.data.local

import android.arch.persistence.room.TypeConverter

/**
 * Created by jastrelax on 2017/11/2.
 */
class StringArrayConverter {

    @TypeConverter
    fun fromArray(array: Array<String>): String {
        val sb = StringBuilder()
        for (s in array) {
            sb.append(s)
            sb.append(",")
        }
        return if (sb.lastIndex < 0) "" else sb.deleteCharAt(sb.lastIndex).toString()
    }

    @TypeConverter
    fun toArray(string: String): Array<String>{
        return string.split(",").toTypedArray()
    }
}