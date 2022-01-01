package com.devlee.mymoviediary.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.utils.Constants.CATEGORY_TABLE

@Entity(tableName = CATEGORY_TABLE)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int,
    var category: Category
)