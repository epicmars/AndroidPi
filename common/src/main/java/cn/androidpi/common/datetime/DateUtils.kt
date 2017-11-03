package cn.androidpi.common.datetime

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by jastrelax on 2017/6/17.
 */

object DateUtils {

    val FORMAT_STANDARD: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)

    fun formatStandard(date: Date): String {
        return FORMAT_STANDARD.format(date)
    }

}
