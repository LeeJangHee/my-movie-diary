package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCreateMyDiaryBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

class CreateMyDiaryFragment : BaseFragment<FragmentCreateMyDiaryBinding>() {

    override fun setView() {
        setAppbar()

    }

    override fun getLayoutId(): Int = R.layout.fragment_create_my_diary

    private fun setAppbar() {
        setTitleToolbar(title = "새 게시물")
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.ic_arrow_back) {
            findNavController().popBackStack()
        }
        setMenuToolbar(type = AppToolbarLayout.RIGHT, strId = R.string.test) {
            Toast.makeText(requireContext(), "게시 완료", Toast.LENGTH_SHORT).show()
        }
    }
}