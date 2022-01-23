package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCreateMyDiaryBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.isBottomNav
import com.devlee.mymoviediary.utils.isMainBottomNavLayout
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel

class CreateMyDiaryFragment : BaseFragment<FragmentCreateMyDiaryBinding>() {

    private val createViewModel: ContentCreateViewModel by viewModels()

    override fun setView() {
        setAppbar()
        setOnBackPressed()
        isMainBottomNavLayout.postValue(false)

        binding.apply {
            viewModel = createViewModel.apply {
                // 권한 체크
                deniedPermissionCallback = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + requireActivity().packageName)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }

                // 선택 BottomSheet
                bottomChoiceViewCallback = { bottomChoiceType ->
                    val action = CreateMyDiaryFragmentDirections.actionCreateMyDiaryFragmentToBottomChoiceFragment(bottomChoiceType)
                    findNavController().navigate(action)
                }
            }
            lifecycleOwner = viewLifecycleOwner
        }
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
        setMenuToolbar(type = AppToolbarLayout.RIGHT, strId = R.string.next_kr) {
            Toast.makeText(requireContext(), "게시 완료", Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).isBottomNav(true)
        }
    }
}

enum class BottomChoiceType {
    CONTENT, CATEGORY
}