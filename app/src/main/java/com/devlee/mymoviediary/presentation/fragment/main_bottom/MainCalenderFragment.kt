package com.devlee.mymoviediary.presentation.fragment.main_bottom

import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMainCalenderBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment


class MainCalenderFragment : BaseFragment<FragmentMainCalenderBinding>() {

    override fun setView() {
        setAppbar()

        binding.monthButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainCalenderFragment_to_calenderMonthFragment)
        }
        binding.dayButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainCalenderFragment_to_calenderDayFragment)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_calender

    private fun setAppbar() {
        setTitleToolbar("달력")
    }

}