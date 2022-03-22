package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMainHomeBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.adapter.home.HomeLayoutType
import com.devlee.mymoviediary.presentation.adapter.home.MainHomeAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.*
import com.devlee.mymoviediary.utils.recyclerview.CustomDecoration
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.SortItem
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

    private var diaryLayoutManager: GridLayoutManager? = null
    private val diaryAdapter: MainHomeAdapter by lazy { MainHomeAdapter() }

    private val diaryItemDecoration by lazy {
        CustomDecoration(
            1.toDp(),
            16.toDp(),
            0f,
            requireContext().resources.getColor(R.color.color_efefef, null)
        )
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_home

    override fun setView() {
        diaryLayoutManager = GridLayoutManager(requireContext(), homeViewModel.homeLayoutType.value?.spanCount ?: 1)
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
                    if (res.data.isNullOrEmpty()) {
                        binding.homeNoDataText.visibility = View.VISIBLE
                    } else {
                        binding.homeNoDataText.visibility = View.GONE
                        with(binding.mainHomeRecyclerView) {
                            post { smoothScrollToPosition(0) }
                        }
                        diaryAdapter.setData(res.data)
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
            layoutManager = diaryLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    when {
                        dy < 0 && binding.addDiaryButton.visibility != View.VISIBLE -> {
                            // 위로 스크롤
                            binding.addDiaryButton.show()
                        }
                        dy > 0 && binding.addDiaryButton.visibility == View.VISIBLE -> {
                            // 아래로 스크롤
                            binding.addDiaryButton.hide()
                        }
                    }
                }
            })
        }

        // 레이아웃 타입 변경
        homeViewModel.homeLayoutType.observe(viewLifecycleOwner) { homeType ->
            val recyclerLayoutManager = binding.mainHomeRecyclerView.layoutManager
            val recyclerAdapter = binding.mainHomeRecyclerView.adapter
            if (recyclerLayoutManager is GridLayoutManager) {
                recyclerLayoutManager.spanCount = homeType.spanCount
                diaryAdapter.setLayoutManager(recyclerLayoutManager)
                recyclerAdapter?.notifyItemRangeChanged(0, recyclerAdapter.itemCount)

                // decoration homeType에 따라 생성 및 제거
                if (homeType.spanCount == HomeLayoutType.GRID.spanCount) {
                    binding.mainHomeRecyclerView.removeItemDecoration(diaryItemDecoration)
                } else {
                    binding.mainHomeRecyclerView.addItemDecoration(diaryItemDecoration)
                }
            }
        }

        // 정렬 타입 변경
        homeViewModel.homeSortType.observe(viewLifecycleOwner) { homeSortItem ->
            homeViewModel.readMyDiary()
        }
    }

    private fun setAppbar() {
        setTitleToolbar(title = "12월", subTitle = "BC12의 ", rightImage = R.drawable.down_arrow_icon) {
            Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()
        }
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.search_icon) {
            Toast.makeText(it.context, "left", Toast.LENGTH_SHORT).show()
        }

        val rightMenuView = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(0, 0, 11.dp(), 0)
        }
        val layoutImageView = ImageView(requireContext()).apply {
            isSelected = homeViewModel.homeLayoutType.value == HomeLayoutType.GRID
            // 벡터 이미지 애니메이션 적용
            setImageDrawable(getDrawable(context, R.drawable.appbar_layout_v_image_icon))
            setPadding(5.dp())
            setOnClickListener {
                isSelected = !isSelected
                delayUiThread(500) {
                    binding.addDiaryButton.show()
                    if (isSelected)
                        homeViewModel.homeLayoutType.postValue(HomeLayoutType.GRID)
                    else
                        homeViewModel.homeLayoutType.postValue(HomeLayoutType.LINEAR)
                }
            }
        }
        val rangeImageView = ImageView(requireContext()).apply {
            load(R.drawable.range_icon) {
                scale(Scale.FILL)
            }
            setPadding(5.dp())
            setOnClickListener {
                if (binding.homeSortText.visibility != View.GONE) {
                    Log.d(TAG, "sort click ignore")
                    return@setOnClickListener
                }
                binding.homeSortText.show()
                delayUiThread(2000) {
                    binding.homeSortText.gone()
                }
                val currentHomeSortType = homeViewModel.homeSortType.value ?: SortItem.DESC
                homeViewModel.homeSortType.value = if (currentHomeSortType == SortItem.DESC) SortItem.ASC else SortItem.DESC
                Log.v(TAG, "homeSortType : ${homeViewModel.homeSortType.value}")
            }
        }
        rightMenuView.addView(layoutImageView)
        rightMenuView.addView(rangeImageView)
        setMenuToolbar(type = AppToolbarLayout.RIGHT, view = rightMenuView)
    }

}