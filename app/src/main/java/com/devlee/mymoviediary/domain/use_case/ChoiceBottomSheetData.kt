package com.devlee.mymoviediary.domain.use_case

import android.os.Parcelable
import com.devlee.mymoviediary.data.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChoiceBottomSheetData(
    val contentType: ContentType? = null,
    val category: Category? = null
) : Parcelable

enum class ContentType(val text: String) {
    VIDEO("영상"),
    AUDIO("음성")
}