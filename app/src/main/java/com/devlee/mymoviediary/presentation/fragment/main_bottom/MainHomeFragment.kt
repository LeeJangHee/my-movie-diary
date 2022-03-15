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
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMainHomeBinding
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.adapter.home.MainHomeAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.*
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
                    if (res.data.isNullOrEmpty()) {
                        binding.homeNoDataText.visibility = View.VISIBLE
                    } else {
                        binding.homeNoDataText.visibility = View.GONE
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

            setImageDrawable(getDrawable(context, R.drawable.appbar_layout_image_icon))
            setPadding(5.dp())
            setOnClickListener {
                isSelected = !isSelected
                Toast.makeText(it.context, "grid", Toast.LENGTH_SHORT).show()
            }
        }
        val rangeImageView = ImageView(requireContext()).apply {
            load(R.drawable.range_icon) {
                scale(Scale.FILL)
            }
            setPadding(5.dp())
            setOnClickListener {
                Toast.makeText(it.context, "range", Toast.LENGTH_SHORT).show()
            }
        }
        rightMenuView.addView(layoutImageView)
        rightMenuView.addView(rangeImageView)
        setMenuToolbar(type = AppToolbarLayout.RIGHT, view = rightMenuView)
    }

}