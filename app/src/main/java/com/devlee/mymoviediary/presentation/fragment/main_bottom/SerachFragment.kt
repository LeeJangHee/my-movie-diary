package com.devlee.mymoviediary.presentation.fragment.main_bottom

import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentSearchBinding
import com.devlee.mymoviediary.presentation.adapter.search.SearchAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.*
import com.devlee.mymoviediary.utils.recyclerview.CustomDecoration
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory
import kotlinx.coroutines.flow.collectLatest

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    companion object {
        private const val TAG = "SearchFragment"
    }

    private var searchText: String? = null
    private var isInitFlag = true

    private val searchViewModel: MyDiaryViewModel by viewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }
    private val args: SearchFragmentArgs by navArgs()
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter(args.searchType) }

    private val searchItemDecoration: CustomDecoration by lazy {
        CustomDecoration(
            height = 1.toDp(),
            paddingLeft = 16.toDp(),
            paddingRight = 0f,
            color = getColorRes(requireContext(), R.color.color_efefef)
        )
    }

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun setView() {
        isInitFlag = true
        setAppbar()
        setOnBackPressed()
        Log.v(TAG, "Search Type :: ${args.searchType.name}")
        setSearchRecyclerView()
        initSearchData()
    }

    private fun initSearchData() = lifecycleScope.launchWhenResumed {
        with(searchViewModel) {
            when (args.searchType) {
                SearchType.Main -> {
                    searchMyDiary(searchText)
                    searchMyDiaryFlow.collectLatest { resMyDiary ->
                        when (resMyDiary) {
                            is Resource.Loading -> loadingLiveData.postValue(true)
                            is Resource.Success -> {
                                loadingLiveData.postValue(false)

                                setSearchView(resMyDiary.data)
                            }
                            is Resource.Error -> {
                                loadingLiveData.postValue(false)
                                Log.e(TAG, "searchMyDiaryFlow: ${resMyDiary.message}")
                            }
                        }
                    }
                }
                SearchType.Category -> {
                    searchCategory(searchText)
                    searchCategoryFlow.collectLatest { resCategory ->
                        when (resCategory) {
                            is Resource.Loading -> loadingLiveData.postValue(true)
                            is Resource.Success -> {
                                loadingLiveData.postValue(false)
                                
                                setSearchView(resCategory.data)
                            }
                            is Resource.Error -> {
                                loadingLiveData.postValue(false)
                                Log.e(TAG, "searchCategoryFlow: ${resCategory.message}")
                            }
                        }
                    }
                }
            }
        }

    }

    private fun setSearchRecyclerView() {
        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(searchItemDecoration)
        }
    }

    private fun setAppbar() {
        setSearchToolbar(editorActionListener = { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (textView.text.isNullOrEmpty()) {
                    Toast.makeText(textView.context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    false
                } else {
                    searchText = textView.text.toString()
                    Log.i(TAG, "editorActionListener: $searchText")
                    when (args.searchType) {
                        SearchType.Main -> searchViewModel.searchMyDiary(searchText)
                        SearchType.Category -> searchViewModel.searchCategory(searchText)
                    }
                    textView.hideKeyboardIME()
                    true
                }
            } else {
                false
            }
        })
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.back_icon) {
            handleBackPressed()
        }
    }

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })
    }

    private fun handleBackPressed() {
        findNavController().popBackStack()
    }

    private fun <T> setSearchView(data: List<T>?) {
        Log.d(TAG, "setSearchView: data size = ${data?.size ?: null}")
        when {
            isInitFlag -> {
                binding.searchEmptyText.show()
                binding.searchEmptyText.text = getString(R.string.search_init_data_text)
                binding.searchRecyclerView.gone()
                isInitFlag = false
            }
            data.isNullOrEmpty() -> {
                binding.searchEmptyText.show()
                binding.searchEmptyText.text = getString(R.string.search_empty_data_text)
                binding.searchRecyclerView.gone()
            }
            else -> {
                binding.searchEmptyText.gone()
                binding.searchRecyclerView.show()
                if (args.searchType == SearchType.Main) {
                    searchAdapter.setSearchMainList(data as List<Pair<MyDiary, Category?>>)
                } else {
                    searchAdapter.setSearchCategoryItemList(data as List<Category>)
                }
            }
        }
    }
}

enum class SearchType {
    Main,
    Category
}