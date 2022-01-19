package com.devlee.mymoviediary.domain.use_case

import java.io.File

data class ContentChoiceData(
    val itemType: Int,
    val video: File? = null,
    val audio: File? = null
)