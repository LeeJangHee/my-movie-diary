package com.devlee.mymoviediary.domain.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.utils.Constants.MEDIA_PAGE_SIZE
import com.devlee.mymoviediary.utils.paging.MediaPagingSource
import com.devlee.mymoviediary.viewmodels.SortItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MediaPagingRepository(
    private val context: Context,
    private val type: ContentType
) {

    companion object {
        private const val TAG = "MediaPagingRepository"
    }

    fun getMediaPagingData(sortItem: SortItem): Flow<PagingData<ContentChoiceFileData>> {
        return Pager(PagingConfig(pageSize = MEDIA_PAGE_SIZE, enablePlaceholders = false)) {
            val items = if (type == ContentType.VIDEO) loadVideo(sortItem) else loadVideo(sortItem)
            Log.d(TAG, "getMediaPagingData: ${items.size}")
            MediaPagingSource(items)
        }.flow
    }

    private fun loadVideo(sortItem: SortItem): List<ContentChoiceFileData> {

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE
        )

        val videoList = mutableListOf<ContentChoiceFileData>()

        return context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_MODIFIED} ${sortItem.order}"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                Log.d(TAG, "loadVideo: $contentUri, $id, $displayName, $mimeType")
                try {
                    videoList.add(ContentChoiceFileData(video = contentUri))
                } catch (e: Exception) {
                    Log.e(TAG, "loadVideo-(): ", e)
                    e.printStackTrace()
                }
            }
            Log.d(TAG, "loadVideo: size ${videoList.size}")
            videoList
        } ?: run {
            Log.d(TAG, "loadVideo: size empty")
            listOf()

        }
    }

}