package com.devlee.mymoviediary.presentation.fragment.main_bottom

import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMainProfileBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

class MainProfileFragment : BaseFragment<FragmentMainProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_main_profile

    override fun setView() {
        setTitleToolbar(title = "Profile")
    }
}