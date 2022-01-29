package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableChar
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.DialogCalendarBinding
import com.devlee.mymoviediary.utils.dialog.CalendarChoiceInterface
import com.devlee.mymoviediary.utils.dialog.DayViewContainer
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.toDp
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@SuppressLint("NewApi")
class CalendarDialogFragment: DialogFragment() {

    private var _binding: DialogCalendarBinding? = null
    private val binding get() = _binding!!

    private val calendarDialogViewModel: ContentCreateViewModel by viewModels()

    private var selectedDate = LocalDate.now()

    private var date = ObservableField<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.Dialog_Base)
        date.set("${selectedDate.year} ${selectedDate.month.ordinal + 1}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogCalendarBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            dateTitle = date.get()
            buttonClickListener = object : CalendarChoiceInterface {
                override fun onPositiveClickListener() {
                    Toast.makeText(requireContext(), "확인", Toast.LENGTH_SHORT).show()
                }

                override fun onNegativeClickListener() {
                    dismiss()
                }

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCornerRadius(view)
        with(binding) {
            setupCalendarView()
            setPositiveButton()
        }
    }

    private fun setCornerRadius(view: View) {
        val model = ShapeAppearanceModel().toBuilder().apply {
            setAllCornerSizes(16.toDp())
        }.build()

        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = getColorRes(requireContext(), R.color.white)
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        view.background = shape
    }

    private fun DialogCalendarBinding.setupCalendarView() {

        val now: YearMonth = YearMonth.now()
        val start = now.minusYears(10)
        val end = now

        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarMain.setup(start, end, firstDayOfWeek)
        calendarMain.scrollToDate(LocalDate.now())

        setupDay()
        setupMonthScrollListener()
    }

    private fun DialogCalendarBinding.setPositiveButton() {
        calendarOk.setTextColor(getColorRes(requireContext(), R.color.color_4a8fff))
    }

    private fun setupMonthScrollListener() {
        binding.calendarMain.monthScrollListener = { month ->
            val day: CalendarDay = month.weekDays.first().first { it.owner == DayOwner.THIS_MONTH }
            date.set("${day.date.year} ${day.date.month.ordinal + 1}")
            binding.dateTitle = date.get()
        }
    }

    private fun setupDay() {
        binding.calendarMain.dayBinder = object : DayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.day.toString()
                if (day.owner != DayOwner.THIS_MONTH) {
                    container.textView.text = null
                }
            }
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
        }
    }
}