package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.model.Mood
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMoodBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.isBottomNav
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory


class MoodFragment : BaseFragment<FragmentMoodBinding>() {

    private val moodViewModel: ContentCreateViewModel by navGraphViewModels(R.id.home_nav)
    private val myDiaryViewModel by viewModels<MyDiaryViewModel> {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }

    override fun setView() {
        setAppbar()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = moodViewModel.apply {

            }
            moodClickListener = View.OnClickListener(::setMoodClickListener)
        }
    }

    private fun setMoodClickListener(view: View) {
        when (view.id) {
            R.id.moodNone -> moodViewModel.mood.set(Mood.NONE)
            R.id.moodSad -> moodViewModel.mood.set(Mood.SAD)
            R.id.moodAngry -> moodViewModel.mood.set(Mood.ANGRY)
            R.id.moodFine -> moodViewModel.mood.set(Mood.FINE)
            R.id.moodGood -> moodViewModel.mood.set(Mood.GOOD)
            R.id.moodHappy -> moodViewModel.mood.set(Mood.HAPPY)
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