package com.devlee.mymoviediary.presentation.fragment.main_bottom.calender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCalenderMonthBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout


class CalenderMonthFragment : BaseFragment<FragmentCalenderMonthBinding>() {
    override fun setView() {
        setAppbar()

        binding.monthCalenderDayButton.setOnClickListener {
            findNavController().navigate(R.id.action_calenderMonthFragment_to_calenderDayFragment)
        }
    }

    private fun setAppbar() {
        setTitleToolbar("Month")
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.ic_arrow_back) {
            findNavController().popBackStack()
        }
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.mipmap.ic_launcher_round) {
            Toast.makeText(requireContext(), "검색클릭", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_calender_month

}