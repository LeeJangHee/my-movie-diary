package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.view.View
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMainHomeBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

class MainHomeFragment : BaseFragment<FragmentMainHomeBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_main_home

    override fun setView() {
        setTitleToolbar(title = "2021. 12", rightImage = R.mipmap.ic_launcher)
        setMenuToolbar(type = AppToolbarLayout.RIGHT, resId = R.mipmap.ic_launcher)
    }

}