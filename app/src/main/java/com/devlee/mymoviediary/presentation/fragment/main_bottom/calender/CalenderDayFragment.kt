package com.devlee.mymoviediary.presentation.fragment.main_bottom.calender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCalenderDayBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout


class CalenderDayFragment : BaseFragment<FragmentCalenderDayBinding>() {
    override fun setView() {
        setAppbar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_calender_day

    private fun setAppbar() {
        setTitleToolbar("Today")
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.ic_arrow_back) {
            findNavController().popBackStack()
        }
    }

}