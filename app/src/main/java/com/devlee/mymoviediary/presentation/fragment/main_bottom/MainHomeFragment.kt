package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMainHomeBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

class MainHomeFragment : BaseFragment<FragmentMainHomeBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_main_home

    override fun setView() {
        setAppbar()

        binding.addDiaryButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainHomeFragment_to_createMyDiaryFragment)
        }
    }

    private fun setAppbar() {
        setTitleToolbar(title = "12월", subTitle = "BC12의 ", rightImage = R.drawable.down_arrow_icon) {
            Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
        }
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.search_icon) {

        }
        setMenuToolbar(type = AppToolbarLayout.RIGHT, resId = R.drawable.range_icon) {

        }
    }

}