package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMainHomeBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.adapter.home.MainHomeAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.Resource
import com.devlee.mymoviediary.utils.isBottomNav
import com.devlee.mymoviediary.utils.isMainBottomNavLayout
import com.devlee.mymoviediary.utils.loadingLiveData
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainHomeFragment : BaseFragment<FragmentMainHomeBinding>() {

    companion object {
        private const val TAG = "MainHomeFragment"
    }

    private val homeViewModel: MyDiaryViewModel by viewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }

    private val diaryAdapter: MainHomeAdapter by lazy { MainHomeAdapter() }

    override fun getLayoutId(): Int = R.layout.fragment_main_home

    override fun setView() {
        setAppbar()
        setRecyclerView()
        isMainBottomNavLayout.value = true

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
        }

        // 추가 버튼
        binding.addDiaryButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainHomeFragment_to_createMyDiaryFragment)
            lifecycleScope.launch {
                delay(300)
                (requireActivity() as MainActivity).isBottomNav(false)
            }
        }
    }

    private fun setRecyclerView() {
        homeViewModel.readMyDiary()
        homeViewModel.handlerMyDiaryList.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Loading -> loadingLiveData.postValue(true)
                is Resource.Success -> {
                    loadingLiveData.postValue(false)
                    if (res.data!!.isNullOrEmpty()) {
                        binding.homeNoDataText.visibility = View.VISIBLE
                    } else {
                        binding.homeNoDataText.visibility = View.GONE
                        diaryAdapter.setData(res.data!!)
                    }
                }
                is Resource.Error -> {
                    loadingLiveData.postValue(false)
                    Log.d(TAG, "${res.message}")
                }
            }
        }
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