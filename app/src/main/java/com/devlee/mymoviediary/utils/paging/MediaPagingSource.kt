package com.devlee.mymoviediary.utils.paging

import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData

class MediaPagingSource(
    override val items: List<ContentChoiceFileData>,
    override val pageSize: Int
) : BasePagingSource<ContentChoiceFileData>() {

    companion object {
        private const val TAG = "MediaPagingSource"
    }
}