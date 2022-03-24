package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMainCategoryBinding
import com.devlee.mymoviediary.presentation.adapter.category.MainCategoryAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.Resource
import com.devlee.mymoviediary.utils.categoryErrorView
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.loadingLiveData
import com.devlee.mymoviediary.utils.recyclerview.CategoryTouchCallback
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class MainCategoryFragment : BaseFragment<FragmentMainCategoryBinding>() {
    private val TAG = "MainCategoryFragment"


    private val categoryViewModel: MyDiaryViewModel by viewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }

    private val categoryAdapter by lazy { MainCategoryAdapter(requireActivity(), categoryViewModel) }
    private val snackbar: Snackbar by lazy { Snackbar.make(binding.root, "", 700) }

    override fun getLayoutId(): Int = R.layout.fragment_main_category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTheme(R.style.AppTheme_Cursor)
    }

    override fun setView() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            categoryViewModel = categoryViewModel
        }

        loadingLiveData.observe(viewLifecycleOwner) {
            if (it) showProgressDialog()
            else dismissProgressDialog()
        }

        setAppbar()
        initList()
        setRecyclerView()

        categoryErrorView = {
            val errorView = LayoutInflater.from(it.context).inflate(R.layout.layout_category_error_view, null)
            val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
            snackbarLayout.apply {
                setBackgroundColor(getColorRes(it.context, android.R.color.transparent))
                addView(errorView, 0)
            }
            snackbar.anchorView = it
            snackbar.show()
        }
    }

    private fun initList() {
        categoryViewModel.readCategory()
        categoryViewModel.handlerCategoryList.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Loading -> loadingLiveData.postValue(true)
                is Resource.Success -> {
                    loadingLiveData.postValue(false)
                    categoryAdapter.setCategoryList(res.data!!)
                }
                is Resource.Error -> {
                    loadingLiveData.postValue(false)
                    Log.d(TAG, "${res.message}")
                }
            }
        }

    }

    private fun setAppbar() {
        setTitleToolbar(title = getString(R.string.category))
        setMenuToolbar(type = AppToolbarLayout.RIGHT, resId = R.drawable.range_icon) {

        }
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.search_icon) {

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRecyclerView() {
        val categoryTouchCallback = CategoryTouchCallback().apply {
            val swipeMenuWidth = resources.getDimension(R.dimen.dp_56) * 2
            setClamp(swipeMenuWidth)
        }
        ItemTouchHelper(categoryTouchCallback).attachToRecyclerView(binding.categoryRecyclerView)
        categoryAdapter.setItemTouchCallback(categoryTouchCallback)
        binding.categoryRecyclerView.apply {
            adapter = categoryAdapter

            setOnTouchListener { _, _ ->
                categoryTouchCallback.removePreviousClamp(this)
                false
            }
        }
    }
}