package com.devlee.mymoviediary.data.database.entity

import androidx.room.*
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.utils.Constants.CATEGORY_ID_NAME
import com.devlee.mymoviediary.utils.Constants.MYDIARY_CATEGORY_ID_NAME
import com.devlee.mymoviediary.utils.Constants.MYDIARY_TABLE

@Entity(
    tableName = MYDIARY_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = [CATEGORY_ID_NAME],
            childColumns = [MYDIARY_CATEGORY_ID_NAME],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class MyDiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Embedded
    val myDiary: MyDiary,
    @ColumnInfo(name = MYDIARY_CATEGORY_ID_NAME)
    val categoryEntityId: Int
)
