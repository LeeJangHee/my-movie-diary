package com.devlee.mymoviediary.presentation.fragment.main_bottom

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentSearchBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun setView() {
        setAppbar()
        setOnBackPressed()
    }

    private fun setAppbar() {
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.back_icon) {
            handleBackPressed()
        }
    }

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })
    }

    private fun handleBackPressed() {
        findNavController().popBackStack()
    }

}