@file:Suppress("UNCHECKED_CAST")

package com.devlee.mymoviediary.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.domain.repository.MediaPagingRepository

class ViewModelProviderFactory(
    private val myDiaryRepository: MyDiaryRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MyDiaryViewModel::class.java) -> MyDiaryViewModel(myDiaryRepository) as T
            modelClass.isAssignableFrom(MyDiaryDetailViewModel::class.java) -> MyDiaryDetailViewModel(myDiaryRepository) as T
            else -> ContentCreateViewModel(myDiaryRepository) as T
        }
    }
}

class MediaViewModelProviderFactory(
    private val mediaRepository: MediaPagingRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaViewModel(mediaRepository) as T
    }

}