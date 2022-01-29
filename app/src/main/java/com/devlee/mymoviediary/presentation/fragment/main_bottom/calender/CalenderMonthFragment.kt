package com.devlee.mymoviediary.presentation.fragment.main_bottom.calender

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCalendarMonthBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout


class CalenderMonthFragment : BaseFragment<FragmentCalendarMonthBinding>() {
    override fun setView() {
        setAppbar()

        binding.monthCalenderDayButton.setOnClickListener {
            findNavController().navigate(R.id.action_calenderMonthFragment_to_calenderDayFragment)
        }
    }

    private fun setAppbar() {
        setTitleToolbar("Month")
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.mipmap.ic_launcher) {
            findNavController().popBackStack()
        }
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.mipmap.ic_launcher_round) {
            Toast.makeText(requireContext(), "검색클릭", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_calendar_month

}