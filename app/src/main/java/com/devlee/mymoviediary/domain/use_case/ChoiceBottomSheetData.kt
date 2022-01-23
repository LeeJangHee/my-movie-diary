package com.devlee.mymoviediary.domain.use_case

import android.os.Parcelable
import com.devlee.mymoviediary.data.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChoiceBottomSheetData(
    val text: String? = null,
    val category: Category? = null
) : Parcelable
