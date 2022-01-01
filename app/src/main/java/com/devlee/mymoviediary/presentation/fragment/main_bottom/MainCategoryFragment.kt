package com.devlee.mymoviediary.presentation.fragment.main_bottom

import androidx.fragment.app.activityViewModels
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMainCategoryBinding
import com.devlee.mymoviediary.presentation.adapter.category.MainCategoryAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory

class MainCategoryFragment : BaseFragment<FragmentMainCategoryBinding>() {


    val categoryViewModel: MyDiaryViewModel by activityViewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }

    private val categoryAdapter by lazy { MainCategoryAdapter() }

    override fun getLayoutId(): Int = R.layout.fragment_main_category

    override fun setView() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            categoryViewModel = categoryViewModel
        }
        setAppbar()
        initList()
        binding.categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initList() {
        categoryViewModel.readCategory()
        categoryViewModel.handlerCategoryList.observe(viewLifecycleOwner) {
            categoryAdapter.setCategoryList(it)
        }

    }

    private fun setAppbar() {
        setTitleToolbar(title = getString(R.string.category))
        setMenuToolbar(type = AppToolbarLayout.RIGHT, resId = R.drawable.range_icon) {

        }
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.search_icon) {

        }
    }
}