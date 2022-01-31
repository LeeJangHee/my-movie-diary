package com.devlee.mymoviediary.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("SimpleDateFormat", "NewApi")
object DateFormatUtil {
    private val DEFAULT_DATE_FORMAT = SimpleDateFormat("yyyy. MM. dd")
    private val fullDate = DateTimeFormatter.ofPattern("yyyy. MM. dd")
    private val monthFormatter = DateTimeFormatter.ofPattern("MM")
    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy. MM")

    fun getTodayFormat(currentTime: Long): String {
        val resultDate = Date(currentTime)
        return DEFAULT_DATE_FORMAT.format(resultDate)
    }

    fun getYearAndMonth(date: LocalDate): String {
        return yearMonthFormatter.format(date)
    }
}