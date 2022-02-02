package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMoodBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.isBottomNav
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel


class MoodFragment : BaseFragment<FragmentMoodBinding>() {

    private val moodViewModel: ContentCreateViewModel by viewModels()

    override fun setView() {
        setAppbar()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = moodViewModel
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_mood

    private fun setAppbar() {
        setTitleToolbar(title = getString(R.string.home_create_toolbar_title))
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.back_icon) {
            findNavController().popBackStack()
        }
        setMenuToolbar(type = AppToolbarLayout.RIGHT, strId = R.string.create_content_btn_text) {
            Toast.makeText(requireContext(), "게시 완료", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_moodFragment_to_mainHomeFragment)
            (requireActivity() as MainActivity).isBottomNav(true)
        }
    }

}