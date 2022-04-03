package com.devlee.mymoviediary.viewmodels

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.presentation.adapter.category.CategoryViewType
import com.devlee.mymoviediary.presentation.adapter.home.HomeLayoutType
import com.devlee.mymoviediary.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyDiaryViewModel(
    private val repository: MyDiaryRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MyDiaryViewModel"
    }

    /** category item */
    var categories: Flow<List<CategoryEntity>> = repository.getCategoryAll()
    var handlerCategoryList: MutableLiveData<Resource<ArrayList<Pair<Category, Int>>>> = MutableLiveData()
    var searchCategoryFlow: MutableSharedFlow<Resource<List<Category>>> = MutableSharedFlow()

    /** category Edit mode */
    val editMode = ObservableBoolean(false)


    /** home item */
    var myDiaries: Flow<List<MyDiaryEntity>> = repository.getMyDiaryAll()
    var handlerMyDiaryList: MutableLiveData<Resource<ArrayList<MyDiary>>> = MutableLiveData()
    var searchMyDiaryFlow: MutableSharedFlow<Resource<List<MyDiary>>> = MutableSharedFlow()

    var homeLayoutType: MutableLiveData<HomeLayoutType> = MutableLiveData(HomeLayoutType.LINEAR)
    var homeSortType: MutableLiveData<SortItem> = MutableLiveData(SortItem.DESC)

    fun searchCategory(title: String?) = viewModelScope.launch(Dispatchers.IO) {
        searchCategoryFlow.emit(Resource.Loading())
        repository.searchCategory(title).collect { categoryEntityList ->
            try {
                searchCategoryFlow.emit(Resource.Success(categoryEntityList.map { it.category }))
            } catch (e: Exception) {
                searchCategoryFlow.emit(Resource.Error(e.localizedMessage ?: "search Category error!!"))
            }
        }
    }

    fun searchMyDiary(contents: String?) = viewModelScope.launch(Dispatchers.IO) {
        searchMyDiaryFlow.emit(Resource.Loading())
        repository.searchMyDiary(contents).collect { myDiaryEntityList ->
            try {
                searchMyDiaryFlow.emit(Resource.Success(myDiaryEntityList.map { it.myDiary }))
            } catch (e: Exception) {
                searchMyDiaryFlow.emit(Resource.Error(e.localizedMessage ?: "search MyDiary error!!"))
            }
        }
    }


    fun readMyDiary() = viewModelScope.launch(Dispatchers.IO) {
        myDiaries.collect { myDiaryEntities ->
            handlerMyDiaryList.postValue(Resource.Loading())
            setMyDiary(myDiaryEntities.map { it.myDiary })
        }
    }

    fun readCategory() = viewModelScope.launch(Dispatchers.IO) {
        categories.collect { categoryEntities ->
            val categoryList = categoryEntities.map { it.category }
            handlerCategoryList.postValue(Resource.Loading())

            SharedPreferencesUtil.setCategoryListPref(categoryList)
            setCategory(categoryList)
        }
    }

    private fun setMyDiary(myDiary: List<MyDiary>) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val sortMyDiary = if (homeSortType.value == SortItem.ASC)
                    myDiary.sortedBy { it.date }
                else
                    myDiary.sortedByDescending { it.date }

                handlerMyDiaryList.postValue(Resource.Success(sortMyDiary.toCollection(ArrayList())))
            } catch (e: Exception) {
                handlerMyDiaryList.postValue(Resource.Error(e.message ?: "home data Error!!!"))
            }
        }
    }

    private fun setCategory(category: List<Category>) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val allCategoryList = arrayListOf<Category>()
                // 기본 카테고리
                val defaultCategory = listOf(
                    Category(
                        title = getStringRes(stringRes = R.string.category_item_all),
                        type = CategoryViewType.DEFAULT.type,
                        color = null,
                        drawableRes = R.drawable.category_all_icon
                    ),
                    Category(
                        title = getStringRes(stringRes = R.string.category_item_favorite),
                        type = CategoryViewType.DEFAULT.type,
                        color = null,
                        drawableRes = R.drawable.star_icon
                    )
                )
                // 카테고리 추가
                val addCategory = listOf(
                    Category(
                        title = getStringRes(stringRes = R.string.category_item_add),
                        type = CategoryViewType.ADD.type,
                        color = null,
                        drawableRes = R.drawable.category_add_icon
                    )
                )

                allCategoryList.addAll(defaultCategory)
                allCategoryList.addAll(category)
                allCategoryList.addAll(addCategory)

                readCategoryCount(allCategoryList)
                val pairCategoryList: ArrayList<Pair<Category, Int>> =
                    allCategoryList.zip(readCategoryCount(allCategoryList)).toCollection(ArrayList())
                handlerCategoryList.postValue(Resource.Success(pairCategoryList))
            } catch (e: Exception) {
                e.printStackTrace()
                handlerCategoryList.postValue(Resource.Error(e.message ?: "Category Error!!!"))
            }
        }
    }

    private fun readCategoryCount(categoryList: List<Category>): ArrayList<Int> {
        val categoryCountList = arrayListOf<Int>()
        categoryList.forEach { category ->
            val categoryCount = repository.getCategoryCount(category)
            Log.e(TAG, "category count = $categoryCount")
            categoryCountList.add(categoryCount)
        }
        Log.e(TAG, "category count list size: ${categoryCountList.size}")
        return categoryCountList
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

    fun onFirstItemClick(view: View) {
        val backgroundColor = view.background
        if (backgroundColor is ColorDrawable) {
            Log.d("firstItemClick", "color = ${backgroundColor.color}")
            categoryFirstItemClick?.invoke(backgroundColor.color)
        }
    }

    fun insertMyDiary(myDiaryEntity: MyDiaryEntity, callback: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMyDiary(myDiaryEntity)
        launch(Dispatchers.Main) {
            callback.invoke()
        }
    }

    fun onUserPickItemClick() {
        categoryUserPickItemClick?.invoke()
    }
}
