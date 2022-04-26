package com.devlee.mymoviediary.data.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val title: String,          // 제목
    val type: Int,              // CategoryAdapter ViewType
    @ColorInt
    val color: Int?,            // 색상
    @DrawableRes
    val drawableRes: Int?       // 이미지
) : Parcelable  {
    override fun toString(): String {
        return """
            [title: $title, type: $type, color: $color, drawableRes: $drawableRes]
        """.trimIndent()
    }
}
