package com.devlee.mymoviediary.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateFormatUtil {
    private val DEFAULT_DATE_FORMAT = SimpleDateFormat("yyyy. MM. dd")

    fun getTodayFormat(currentTime: Long): String {
        val resultDate = Date(currentTime)
        return DEFAULT_DATE_FORMAT.format(resultDate)
    }
}