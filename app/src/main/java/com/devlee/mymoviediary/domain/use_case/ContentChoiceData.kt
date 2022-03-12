package com.devlee.mymoviediary.domain.use_case

import android.net.Uri
import com.devlee.mymoviediary.presentation.adapter.create.CreateViewType

data class ContentChoiceData(           // [새 게시물] - 오디오, 비디오 선택 Item
    val itemType: Int,                  // CreateAdapter ViewType
    val video: Uri? = null,            // 비디오 파일
    val audio: Uri? = null             // 오디오 파일
) {
    companion object {
        fun ContentChoiceData.toContentChoiceFileData(): ContentChoiceFileData {
            return ContentChoiceFileData(video = video, audio = audio)
        }
    }
}

data class ContentChoiceFileData(
    val video: Uri? = null,
    val audio: Uri? = null,
    val title: String? = null
) {
    companion object {
        fun ContentChoiceFileData.toContentChoiceData(): ContentChoiceData {
            return ContentChoiceData(CreateViewType.DIARY.type, video, audio)
        }
    }
}