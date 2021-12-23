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
        setTitleToolbar(title = "2021. 12", rightImage = R.mipmap.ic_launcher) {
            Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
        }
        setMenuToolbar(type = AppToolbarLayout.RIGHT, resId = R.mipmap.ic_launcher)
    }

}