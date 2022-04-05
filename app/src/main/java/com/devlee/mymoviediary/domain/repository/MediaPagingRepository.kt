package com.devlee.mymoviediary.domain.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.utils.Constants.MEDIA_PAGE_SIZE
import com.devlee.mymoviediary.utils.FileUtil
import com.devlee.mymoviediary.utils.paging.MediaPagingSource
import com.devlee.mymoviediary.viewmodels.SortItem
import kotlinx.coroutines.flow.Flow

class MediaPagingRepository(
    private val context: Context,
    private val type: ContentType
) {

    companion object {
        private const val TAG = "MediaPagingRepository"
    }

    fun getMediaPagingData(sortItem: SortItem): Flow<PagingData<ContentChoiceFileData>> {
        return Pager(PagingConfig(pageSize = MEDIA_PAGE_SIZE, enablePlaceholders = false)) {
            val items = if (type == ContentType.VIDEO) loadVideo(sortItem) else loadAudio(sortItem)
            Log.d(TAG, "getMediaPagingData: ${items.size}")
            MediaPagingSource(items, MEDIA_PAGE_SIZE)
        }.flow
    }

    private fun loadVideo(sortItem: SortItem): List<ContentChoiceFileData> {

        val collection = FileUtil.getVideoCollection()
        val projection = FileUtil.getVideoProjection()

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
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val title = cursor.getString(titleColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                Log.d(TAG, "loadVideo: $contentUri, $id, $displayName, $mimeType")
                try {
                    videoList.add(ContentChoiceFileData(video = contentUri, title = title))
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

    private fun loadAudio(sortItem: SortItem): List<ContentChoiceFileData> {
        val collection = FileUtil.getAudioCollection()
        val projection = FileUtil.getAudioProjection()

        val audioList = mutableListOf<ContentChoiceFileData>()

        return context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.DATE_MODIFIED} ${sortItem.order}"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val title = cursor.getString(titleColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                Log.d(TAG, "loadAudio: $contentUri, $id, $displayName, $mimeType , $title")
                try {
                    audioList.add(ContentChoiceFileData(audio = contentUri, title = title))
                } catch (e: Exception) {
                    Log.e(TAG, "loadAudio-(): ", e)
                    e.printStackTrace()
                }
            }
            Log.d(TAG, "loadAudio: size ${audioList.size}")
            audioList
        } ?: run {
            Log.d(TAG, "loadAudio: size Empty")
            listOf()
        }
    }

}