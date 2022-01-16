package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCreateMyDiaryBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.isBottomNav

class CreateMyDiaryFragment : BaseFragment<FragmentCreateMyDiaryBinding>() {

    override fun setView() {
        setAppbar()
        setOnBackPressed()

    }

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                (requireActivity() as MainActivity).isBottomNav(true)
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_my_diary

    private fun setAppbar() {
        setTitleToolbar(title = getString(R.string.home_create_toolbar_title))
        setMenuToolbar(type = AppToolbarLayout.LEFT, strId = R.string.no_kr) {
            findNavController().popBackStack()
            (requireActivity() as MainActivity).isBottomNav(true)
        }
        setMenuToolbar(type = AppToolbarLayout.RIGHT, strId = R.string.test) {
            Toast.makeText(requireContext(), "게시 완료", Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).isBottomNav(true)
        }
    }
}