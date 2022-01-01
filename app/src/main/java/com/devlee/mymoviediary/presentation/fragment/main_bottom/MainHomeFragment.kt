package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMainHomeBinding
import com.devlee.mymoviediary.presentation.adapter.home.MainHomeAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory

class MainHomeFragment : BaseFragment<FragmentMainHomeBinding>() {

    val homeViewModel: MyDiaryViewModel by activityViewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }

    private val diaryAdapter: MainHomeAdapter by lazy { MainHomeAdapter() }

    override fun getLayoutId(): Int = R.layout.fragment_main_home

    override fun setView() {
        setAppbar()
        setRecyclerView()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        // 추가 버튼
        binding.addDiaryButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainHomeFragment_to_createMyDiaryFragment)
        }
    }

    private fun setRecyclerView() {
        val newList = listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        diaryAdapter.setData(newList)
        binding.mainHomeRecyclerView.apply {
            adapter = diaryAdapter
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