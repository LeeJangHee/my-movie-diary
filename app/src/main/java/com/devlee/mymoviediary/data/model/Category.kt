package com.devlee.mymoviediary.data.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val title: String,
    val type: Int,
    @ColorInt
    val color: Int? = null,
    @DrawableRes
    val drawableRes: Int? = null
): Parcelable
