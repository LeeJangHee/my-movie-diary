package com.devlee.mymoviediary.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MyDiaryDetailViewModel(val repository: MyDiaryRepository) : ViewModel() {

    companion object {
        private const val TAG = "MyDiaryDetailViewModel"
    }

    /** myDiary Detail */
    private var _myDiaryDetail: MutableSharedFlow<MyDiary> = MutableSharedFlow()
    val myDiaryDetail get() = _myDiaryDetail.asSharedFlow()

    private var _myDiaryDetailCategory: MutableSharedFlow<Category?> = MutableSharedFlow()
    val myDiaryDetailCategory get() = _myDiaryDetailCategory.asSharedFlow()


    fun setMyDiaryDetail(myDiary: MyDiary) = viewModelScope.launch(Dispatchers.IO) {
        _myDiaryDetail.emit(myDiary)
    }

    fun setMyDiaryDetailCategory(category: Category?) = viewModelScope.launch(Dispatchers.IO) {
        _myDiaryDetailCategory.emit(category)
    }

    fun updateMyDiary(myDiaryId: Int, categoryId: Int?, myDiary: MyDiary) = viewModelScope.launch(Dispatchers.IO) {
        Log.d(TAG, "updateMyDiary() called")
        repository.updateMyDiary(
            id = myDiaryId,
            categoryId = categoryId,
            myDiary = myDiary
        )
    }


    fun getCategoryId(category: Category?): Int? {
        Log.d(TAG, "getCategoryId() called with: category = $category")
        category ?: return null
        return repository.getCategoryId(category)
    }

    fun getMyDiaryId(myDiary: MyDiary?): Int? {
        Log.d(TAG, "getMyDiaryId() called with: myDiary = $myDiary")
        myDiary ?: return null
        return repository.getMyDiaryId(myDiary)
    }

    fun deleteMyDiary(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMyDiary(id)
    }
}