package cn.androidpi.common.datetime

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jastrelax on 2017/6/17.
 */

object DateUtils {

    val FORMAT_STANDARD: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)
    val FORMAT_DATE: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)

    fun formatStandard(date: Date): String {
        return FORMAT_STANDARD.format(date)
    }

    fun formatDate(date: Date): String {
        return FORMAT_DATE.format(date)
    }

}
