package com.devlee.mymoviediary.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.devlee.mymoviediary.domain.repository.MediaPagingRepository

class MediaViewModel(
    private val repository: MediaPagingRepository
) : ViewModel() {


    private val sortItemLiveData: MutableLiveData<SortItem> = MutableLiveData()

    val mediaPagingData = sortItemLiveData.switchMap {
        repository.getMediaPagingData(it).cachedIn(viewModelScope).asLiveData()
    }

    fun setSortItemFlow(sortItem: SortItem) {
        sortItemLiveData.value = sortItem
    }
}