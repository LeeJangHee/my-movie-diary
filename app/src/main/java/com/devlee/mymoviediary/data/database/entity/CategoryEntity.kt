package com.devlee.mymoviediary.data.database.entity

import androidx.room.*
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.utils.Constants.CATEGORY_COLUMN_NAME
import com.devlee.mymoviediary.utils.Constants.CATEGORY_TABLE

@Entity(tableName = CATEGORY_TABLE)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int,
    val title: String,
    @ColumnInfo(name = CATEGORY_COLUMN_NAME)
    val category: Category
)