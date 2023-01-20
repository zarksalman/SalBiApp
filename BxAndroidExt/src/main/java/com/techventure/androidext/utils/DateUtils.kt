package com.techventure.androidext.utils


import android.annotation.SuppressLint
import com.techventure.androidext.extEnums.ExtEnums
import com.techventure.androidext.utils.DateConstants.DD_dot_MM_dot_YYYY
import com.techventure.androidext.utils.DateConstants.UTC_PATTERN_WITHOUT_ZONE
import com.techventure.androidext.utils.ExtConstant.EMPTY_STRING
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


fun getCurrentDateStringFormat(): String? {
    val dateFormat = SimpleDateFormat(DD_dot_MM_dot_YYYY)
    return dateFormat.format(getCurrentDeviceTime())
}

fun getCurrentDeviceTime(): Date {
    return Calendar.getInstance().time
}

fun getTimerText(time: Double): String? {
    getTimeDetails(time).apply {
        return get(ExtEnums.SECONDS.key)?.let { seconds ->
            get(ExtEnums.MINUTES.key)?.let { minutes ->
                get(ExtEnums.HOURS.key)?.let { hours ->
                    formatTime(seconds, minutes, hours)
                }
            }
        }
    }
}

fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
    DateConstants.apply {
        return String.format(TIMER_DISPLAY_FORMAT, hours) + ":" +
                String.format(TIMER_DISPLAY_FORMAT, minutes) + ":" +
                String.format(TIMER_DISPLAY_FORMAT, seconds)
    }
}

fun getReportTimeText(time: Double): String? {
    getTimeDetails(time).apply {
        return get(ExtEnums.SECONDS.key)?.let { seconds ->
            get(ExtEnums.MINUTES.key)?.let { minutes ->
                get(ExtEnums.HOURS.key)?.let { hours ->
                    formatTimeWithDetails(seconds, minutes, hours)
                }
            }
        }
    }
}

fun formatTimeWithDetails(seconds: Int, minutes: Int, hours: Int): String {
    DateConstants.apply {
        return String.format(TIMER_DETAILS_DISPLAY_FORMAT, hours) + "h " +
                String.format(TIMER_DISPLAY_FORMAT, minutes) + "m " +
                String.format(TIMER_DISPLAY_FORMAT, seconds) + "s"
    }
}

private fun getTimeDetails(time: Double): Map<String, Int> {
    val rounded = Math.round(time).toInt()
    val seconds = rounded % 86400 % 3600 % 60
    val minutes = rounded % 86400 % 3600 / 60
    val hours = rounded % 86400 / 3600
    return mapOf(
        ExtEnums.SECONDS.key to seconds,
        ExtEnums.MINUTES.key to minutes,
        ExtEnums.HOURS.key to hours
    )
}

@SuppressLint("SimpleDateFormat")
fun timeDifference(startDateTime: Date?, endDateTime: Date?): Long {
    val sdf = SimpleDateFormat(DateConstants.TIME_DIFFERENCE_TIME_FORMAT)

    val date1 = sdf.parse(sdf.format(startDateTime))
    val date2 = sdf.parse(sdf.format(endDateTime))

    return abs(date1.time - date2.time)
}

private fun getTimeDifference(mills: Long): Map<String, Int> {
    val hours = (mills / (1000 * 60 * 60)).toInt()
    val minutes = (mills / (1000 * 60)).toInt() % 60
    val seconds = ((mills / 1000).toInt() % 60)
    return mapOf(
        ExtEnums.SECONDS.key to seconds,
        ExtEnums.MINUTES.key to minutes,
        ExtEnums.HOURS.key to hours
    )
}

fun timeDifferenceString(mills: Long): String? {
    getTimeDifference(mills).apply {
        return get(ExtEnums.SECONDS.key)?.let { seconds ->
            get(ExtEnums.MINUTES.key)?.let { minutes ->
                get(ExtEnums.HOURS.key)?.let { hours ->
                    formatTime(seconds, minutes, hours)
                }
            }
        }
    }
}

fun timeDetailsDifferenceString(mills: Long): String? {
    getTimeDifference(mills).apply {
        return get(ExtEnums.SECONDS.key)?.let { seconds ->
            get(ExtEnums.MINUTES.key)?.let { minutes ->
                get(ExtEnums.HOURS.key)?.let { hours ->
                    formatTimeWithDetails(seconds, minutes, hours)
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getTimeConcatString(
    dateTime: Date?,
    hour: String?,
    mint: String?,
    locale: String? = "en"
): Map<String, Any> {
    DateConstants.apply {
        val dateString = dateTime.formatDateIntoString(YYYY_MM__DD, false)
        val time = "$dateString $hour:$mint"
        val date = SimpleDateFormat(YYYY_MM_DD_HH_MM, Locale(locale, "")).parse(
            time
        )

        return mapOf(
            ExtEnums.TIME_STRING.key to date.formatDateIntoString(HH_mm),
            ExtEnums.DATE.key to date
        )
    }
}

fun Date?.addTime(dateType: Int, value: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(dateType, value)
    return calendar.time
}

fun Calendar?.addCalender(dateType: Int, value: Int): Calendar? {
    this?.add(dateType, value)
    return this
}

fun Date?.toCalendar(): Calendar? {
    return Calendar.getInstance().apply {
        time = this@toCalendar
    }
}

fun Date?.getDateStringWithoutTimeZone(): String? {
    return formatDateIntoString(UTC_PATTERN_WITHOUT_ZONE).getDateTimeFromDateString(
        UTC_PATTERN_WITHOUT_ZONE
    )?.formatDateIntoString(UTC_PATTERN_WITHOUT_ZONE)
}

fun Date?.formatDateIntoString(
    dateFormatter: String,
    shouldAddLocale: Boolean = true,
    enableTimeZone: Boolean = false,
    locale: String? = "en"
): String {
    val formatter = if (shouldAddLocale) SimpleDateFormat(dateFormatter, Locale(locale, ""))
    else SimpleDateFormat(dateFormatter)

    if (enableTimeZone) {
        formatter.timeZone = TimeZone.getTimeZone(DateConstants.UTC)
    }
    return try {
        formatter.format(this)
    } catch (e: ParseException) {
        EMPTY_STRING
    }
}

fun String?.getDateTimeFromDateString(
    dateFormatter: String,
    shouldAddLocale: Boolean = true,
    timeZoneEnable: Boolean = false,
    locale: String? = "en"
): Date? {
    val dateFormat = if (shouldAddLocale) SimpleDateFormat(
        dateFormatter,
        Locale(locale, "")
    ) else SimpleDateFormat(dateFormatter)
    if (timeZoneEnable) dateFormat.timeZone = TimeZone.getTimeZone(DateConstants.UTC)
    return try {
        dateFormat.parse(this)
    } catch (e: ParseException) {
        null
    }
}

@SuppressLint("SimpleDateFormat")
fun Date?.convertDateToTimeString(
    dateFormatter: String,
    timeFormatter: String
): String {
    DateConstants.apply {
        val dateString = this@convertDateToTimeString.formatDateIntoString(dateFormatter)
        val format = SimpleDateFormat(dateFormatter)
        val dateTime = format.parse(dateString)
        return SimpleDateFormat(timeFormatter, Locale.ENGLISH).format(dateTime)
    }
}


fun getTimeDifferenceInSec(startDate: Date?, currentDate: Date?): Long {
    return when {
        startDate == null -> Long.MAX_VALUE
        currentDate == null -> 0
        else -> (currentDate.time - startDate.time) / 1000
    }
}

fun getCalender(
    date: Date? = Date(),
    add: Int? = 0,
    addType: Int = Calendar.SECOND,
    shouldResetSecond: Boolean = false,
    shouldResetToStartMonth: Boolean = false,
    shouldStartOfDay: Boolean = false,
    shouldEndOfMonth: Boolean = false
): Calendar {
    val calender = Calendar.getInstance()
    calender.time = date ?: Date()
    if (shouldResetSecond) {
        calender.set(Calendar.SECOND, 0)
    }
    if (shouldResetToStartMonth || shouldEndOfMonth) {
        calender.set(Calendar.SECOND, 0)
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.DAY_OF_MONTH, 1)
        calender.set(Calendar.MILLISECOND, 0)
    }
    if (shouldStartOfDay) {
        calender.set(Calendar.SECOND, 0)
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.MILLISECOND, 0)
    }

    if (shouldEndOfMonth) {
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH))

    }

    calender.add(addType, add ?: 0)
    return calender
}

fun Calendar.utcToLocal(): Calendar {
    val ms = time.time + Calendar.getInstance().timeZone
        .getOffset(time.time)
    return getCalender(Date(ms))
}

fun isSameDay(date1: Date?, date2: Date? = Date()): Boolean {
    if (date1 == null || date2 == null) return false
    val currentCalendar1 = Calendar.getInstance()
    currentCalendar1.time = date1
    val currentCalendar2 = Calendar.getInstance()
    currentCalendar2.time = date2
    return currentCalendar1.get(Calendar.MONTH) == currentCalendar2.get(Calendar.MONTH) &&
            currentCalendar1.get(Calendar.YEAR) == currentCalendar2.get(Calendar.YEAR) &&
            currentCalendar1.get(Calendar.DAY_OF_MONTH) == currentCalendar2.get(Calendar.DAY_OF_MONTH)
}

fun isSameMonth(date1: Date?, date2: Date? = Date()): Boolean {
    if (date1 == null || date2 == null) return false
    val currentCalendar1 = Calendar.getInstance()
    currentCalendar1.time = date1
    val currentCalendar2 = Calendar.getInstance()
    currentCalendar2.time = date2
    return currentCalendar1.get(Calendar.MONTH) == currentCalendar2.get(Calendar.MONTH) &&
            currentCalendar1.get(Calendar.YEAR) == currentCalendar2.get(Calendar.YEAR)
}

fun getDaysDifferenceOfDates(date1: Date?, date2: Date?): Long? {
    val millionSeconds = date1?.time?.let { date2?.time?.minus(it) }
    return millionSeconds?.let { TimeUnit.MILLISECONDS.toDays(it) }
}

fun Calendar?.isWeekend(): Boolean {
    this ?: return false
    return get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
}