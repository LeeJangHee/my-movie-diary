package com.devlee.mymoviediary.viewmodels

import androidx.lifecycle.*
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.presentation.adapter.category.CategoryViewType
import com.devlee.mymoviediary.presentation.adapter.category.MainCategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyDiaryViewModel(
    val repository: MyDiaryRepository
) : ViewModel() {

    var categories = repository.getCategoryAll()
    var handlerCategoryList: MutableLiveData<ArrayList<Category>> = MutableLiveData(arrayListOf())

    val categoryAdapter = MainCategoryAdapter()

    fun readCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            categories.collect { categoryEntities ->
                setCategory(categoryEntities.map { it.category })
            }
        }
    }

    fun setCategory(category: List<Category>) {
        viewModelScope.launch(Dispatchers.IO) {
            val allCategoryList = arrayListOf<Category>()
            // 기본 카테고리
            val defaultCategory = listOf(
                Category(title = "전체보기", type = CategoryViewType.DEFAULT.type, drawableRes = R.drawable.category_all_icon),
                Category(title = "즐겨찾기", type = CategoryViewType.DEFAULT.type, drawableRes = R.drawable.star_icon)
            )
            // 카테고리 추가
            val addCategory = listOf(
                Category(title = "카테고리 추가", type = CategoryViewType.ADD.type, drawableRes = R.drawable.category_add_icon)
            )

            allCategoryList.addAll(defaultCategory)
            allCategoryList.addAll(category)
            allCategoryList.addAll(addCategory)

            handlerCategoryList.postValue(allCategoryList)
        }
    }
}
