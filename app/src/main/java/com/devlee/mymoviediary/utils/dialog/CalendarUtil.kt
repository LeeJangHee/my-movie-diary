package com.devlee.mymoviediary.utils.dialog

import android.view.View
import com.devlee.mymoviediary.databinding.ItemCalendarDayBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View, listener: (CalendarDay) -> Unit) : ViewContainer(view) {
    val binding = ItemCalendarDayBinding.bind(view)
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener { listener.invoke(day) }
    }
}

interface CalendarChoiceInterface {
    fun onPositiveClickListener()
    fun onNegativeClickListener()
}


var calendarDialogCallback: ((String) -> Unit)? = null