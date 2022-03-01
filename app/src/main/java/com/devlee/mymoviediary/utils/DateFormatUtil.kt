package com.devlee.mymoviediary.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("SimpleDateFormat", "NewApi")
object DateFormatUtil {
    private val DEFAULT_DATE_FORMAT = SimpleDateFormat("yyyy. MM. dd")
    private val fullDate = DateTimeFormatter.ofPattern("yyyy. MM. dd")
    private val monthFormatter = DateTimeFormatter.ofPattern("MM")
    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy. MM")

    fun getTodayDate(): String {
        return getAllDate(LocalDate.now())
    }

    fun getYearAndMonth(date: LocalDate): String {
        return yearMonthFormatter.format(date)
    }

    fun getAllDate(date: LocalDate): String {
        return fullDate.format(date)
    }

    fun getYear(date: LocalDate): String {
        return yearFormatter.format(date)
    }

    fun getAudioTimeLine(time: Long): String {
        val timelineFormatter = "mm:ss"
        val dateFormat = SimpleDateFormat(timelineFormatter)
        return dateFormat.format(time)
    }
}