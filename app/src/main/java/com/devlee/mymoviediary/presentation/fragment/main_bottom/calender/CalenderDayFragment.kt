package com.devlee.mymoviediary.presentation.fragment.main_bottom.calender

import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCalendarDayBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout


class CalenderDayFragment : BaseFragment<FragmentCalendarDayBinding>() {
    override fun setView() {
        setAppbar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_calendar_day

    private fun setAppbar() {
        setTitleToolbar("Today")
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.mipmap.ic_launcher) {
            findNavController().popBackStack()
        }
    }

}