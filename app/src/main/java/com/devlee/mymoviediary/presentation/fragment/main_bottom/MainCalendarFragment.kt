package com.devlee.mymoviediary.presentation.fragment.main_bottom

import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMainCalendarBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment


class MainCalendarFragment : BaseFragment<FragmentMainCalendarBinding>() {

    override fun setView() {
        setAppbar()

        binding.monthButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainCalenderFragment_to_calenderMonthFragment)
        }
        binding.dayButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainCalenderFragment_to_calenderDayFragment)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_calendar

    private fun setAppbar() {
        setTitleToolbar("달력")
    }

}