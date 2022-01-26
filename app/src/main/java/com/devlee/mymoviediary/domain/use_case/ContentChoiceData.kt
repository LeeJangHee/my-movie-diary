package com.devlee.mymoviediary.domain.use_case

import java.io.File

data class ContentChoiceData(           // [새 게시물] - 오디오, 비디오 선택 Item
    val itemType: Int,                  // CreateAdapter ViewType
    val video: File? = null,            // 비디오 파일
    val audio: File? = null             // 오디오 파일
)