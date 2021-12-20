package com.devlee.mymoviediary.presentation.fragment.main_bottom

import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMainCategoryBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

class MainCategoryFragment : BaseFragment<FragmentMainCategoryBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_main_category

    override fun setView() {
        setTitleToolbar(title = "CATEGORY")
        setMenuToolbar(type = AppToolbarLayout.RIGHT, resId = R.mipmap.ic_launcher)
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.mipmap.ic_launcher)
    }
}