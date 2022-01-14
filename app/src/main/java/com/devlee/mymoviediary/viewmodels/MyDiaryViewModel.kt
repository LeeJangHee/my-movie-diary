package com.devlee.mymoviediary.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.presentation.adapter.category.CategoryViewType
import com.devlee.mymoviediary.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyDiaryViewModel(
    val repository: MyDiaryRepository
) : ViewModel() {

    var categories = repository.getCategoryAll()
    var handlerCategoryList: MutableLiveData<Resource<ArrayList<Category>>> = MutableLiveData()

    val editMode = ObservableBoolean(false)

    fun readCategory() = viewModelScope.launch(Dispatchers.IO) {
        categories.collect { categoryEntitis ->
            handlerCategoryList.postValue(Resource.Loading())
            setCategory(categoryEntitis.map { it.category })
        }
    }

    fun setCategory(category: List<Category>) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val allCategoryList = arrayListOf<Category>()
                // 기본 카테고리
                val defaultCategory = listOf(
                    Category("전체보기", CategoryViewType.DEFAULT.type, null, R.drawable.category_all_icon),
                    Category("즐겨찾기", CategoryViewType.DEFAULT.type, null, R.drawable.star_icon)
                )
                // 카테고리 추가
                val addCategory = listOf(
                    Category("카테고리 추가", CategoryViewType.ADD.type, null, R.drawable.category_add_icon)
                )

                allCategoryList.addAll(defaultCategory)
                allCategoryList.addAll(category)
                allCategoryList.addAll(addCategory)

                handlerCategoryList.postValue(Resource.Success(allCategoryList))
            } catch (e: Exception) {
                e.printStackTrace()
                handlerCategoryList.postValue(Resource.Error(e.message ?: "Category Error!!!"))
            }
        }
    }

    fun insertCategory(categoryEntity: CategoryEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCategory(categoryEntity)
        }

    fun deleteCategory(category: Category) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCategory(category)
        }

    fun updateCategory(category: Category, preCategory: Category) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCategory(category, preCategory)
        }
}
