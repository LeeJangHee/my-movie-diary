package com.devlee.mymoviediary.utils.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.utils.Constants

class MediaPagingSource(
    private val items: List<ContentChoiceFileData>
) : PagingSource<Int, ContentChoiceFileData>() {

    companion object {
        private const val TAG = "MediaPagingSource"
    }

    override fun getRefreshKey(state: PagingState<Int, ContentChoiceFileData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentChoiceFileData> {
        val page = params.key ?: 1
        return try {
            Log.d(TAG, "load: $items")
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.size > page * Constants.MEDIA_PAGE_SIZE) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}