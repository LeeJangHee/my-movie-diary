package com.devlee.mymoviediary.utils.dialog

import android.view.View
import com.devlee.mymoviediary.databinding.ItemCalendarDayBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = ItemCalendarDayBinding.bind(view).calendarDay
}

interface CalendarChoiceInterface {
    fun onPositiveClickListener()
    fun onNegativeClickListener()
}