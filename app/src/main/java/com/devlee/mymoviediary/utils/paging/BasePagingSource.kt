package com.devlee.mymoviediary.utils.paging


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<V : Any> : PagingSource<Int, V>() {

    abstract val items: List<V>
    abstract val pageSize: Int

    companion object {
        private const val TAG = "BasePagingSource"
        const val INITIAL_PAGE_INDEX = 0
    }

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        return try {
            Log.v(TAG, "load() item size = ${items.size}")
            if (items.isNullOrEmpty()) throw NullPointerException("item is Empty")
            LoadResult.Page(
                data = items,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (items.size > page * pageSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}