package com.devlee.mymoviediary.data.database.entity

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.utils.Constants.CATEGORY_TABLE

@Entity(tableName = CATEGORY_TABLE)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int,
    val title: String,
    val type: Int,
    @ColorInt val color: Int?,
    @DrawableRes val drawableRes: Int?,
    val category: Category
)