package com.devlee.mymoviediary.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devlee.mymoviediary.data.repository.MyDiaryRepository

class ViewModelProviderFactory(
    val myDiaryRepository: MyDiaryRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyDiaryViewModel(myDiaryRepository) as T
    }
}