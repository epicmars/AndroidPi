package cn.androidpi.common.datetime

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jastrelax on 2017/6/17.
 */

object DateTimeUtils {

    val FORMAT_STANDARD: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)
    val FORMAT_DATE: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
    val FORMAT_TIME: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINESE)
    val FORMAT_TIME_HH_mm: DateFormat = SimpleDateFormat("HH:mm", Locale.CHINESE)

    fun formatStandard(date: Date): String {
        return FORMAT_STANDARD.format(date)
    }

    fun formatDate(date: Date): String {
        return FORMAT_DATE.format(date)
    }

    fun formatTime(time: Date): String {
        return FORMAT_TIME.format(time)
    }

    fun formatTimeHHmm(time: Date): String {
        return FORMAT_TIME_HH_mm.format(time)
    }

    fun now(): Calendar {
        return Calendar.getInstance()
    }

    fun assembleDateTime(date: Date?, time: Date?): Date {
        if (date == null || time == null)
            return now().time
        val datetime = now()
        val timeCalendar = now()
        datetime.time = date
        timeCalendar.time = time
        datetime.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        datetime.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        datetime.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND))
        datetime.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND))
        return datetime.time
    }

}
