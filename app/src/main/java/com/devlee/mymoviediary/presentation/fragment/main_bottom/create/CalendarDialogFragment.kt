package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.viewModels
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.DialogCalendarBinding
import com.devlee.mymoviediary.presentation.fragment.BaseDialogFragment
import com.devlee.mymoviediary.utils.DateFormatUtil
import com.devlee.mymoviediary.utils.dialog.CalendarChoiceInterface
import com.devlee.mymoviediary.utils.dialog.DayViewContainer
import com.devlee.mymoviediary.utils.dp
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.toDp
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@SuppressLint("NewApi")
class CalendarDialogFragment : BaseDialogFragment<DialogCalendarBinding>(R.layout.dialog_calendar) {

    private val calendarDialogViewModel: ContentCreateViewModel by viewModels()

    private var selectedDate = LocalDate.now()

    private var date = ObservableField<String>()
    private val today = LocalDate.now()

    private lateinit var selectionShape: InsetDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setView() {
        init()
        setCornerRadius(16f)
        binding.apply {
            viewModel = calendarDialogViewModel
            lifecycleOwner = viewLifecycleOwner
            dateTitle = date.get()
            buttonClickListener = object : CalendarChoiceInterface {
                override fun onPositiveClickListener(day: CalendarDay?) {
                    Toast.makeText(requireContext(), "확인", Toast.LENGTH_SHORT).show()
                }

                override fun onNegativeClickListener() {
                    dismiss()
                }

            }
            setupCalendarView()
            setPositiveButton()
        }
    }

    private fun init() {
        val shapeModelRound = ShapeAppearanceModel().toBuilder().apply {
            setAllCorners(CornerFamily.ROUNDED, 45.toDp())
        }.build()

        selectionShape = InsetDrawable(MaterialShapeDrawable(shapeModelRound).apply {
            val backgroundColor = getColorRes(requireContext(), R.color.black)
            fillColor = ColorStateList.valueOf(backgroundColor)
        }, 3.dp())

    }

    private fun DialogCalendarBinding.setupCalendarView() {

        val now: YearMonth = YearMonth.now()
        val start = now.minusYears(10)

        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        calendarMain.setup(start, now, firstDayOfWeek)
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
            date.set(DateFormatUtil.getYearAndMonth(day.date))
            binding.dateTitle = date.get()
        }
    }

    private fun setupDay() {
        binding.calendarMain.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view, ::onDayClick)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.binding.calendarDay.text = day.day.toString()
                container.day = day
                calendarDialogViewModel.day = day

                container.binding.shape.background = null
                container.binding.calendarDay.setTextColor(getColorRes(requireContext(), R.color.color_1c1c1c))

                when {
                    day.owner != DayOwner.THIS_MONTH -> {
                        // 이번 달이 아닌 day 보이지 않기
                        container.binding.calendarDay.text = null
                        container.binding.calendarDay.isClickable = false
                    }
                    today < day.date -> {
                        // 미래는 선택 불가
                        container.binding.calendarDay.setTextColor(getColorRes(requireContext(), R.color.color_999999))
                        container.binding.calendarDay.isClickable = false
                    }
                    selectedDate == day.date -> {
                        // 선택된 day style 변경
                        container.binding.shape.background = selectionShape
                        container.binding.calendarDay.setTextColor(getColorRes(requireContext(), R.color.white))
                    }

                }
            }
        }
    }

    private fun onDayClick(day: CalendarDay) {
        if (day.owner != DayOwner.THIS_MONTH || today < day.date) {
            // We don't want to
            return
        }
        calendarDialogViewModel.day = day
        selectedDate = day.date

        binding.calendarMain.notifyCalendarChanged()
    }
}