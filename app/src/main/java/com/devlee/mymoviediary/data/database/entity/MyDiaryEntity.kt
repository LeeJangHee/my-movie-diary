package com.devlee.mymoviediary.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.utils.Constants.MYDIARY_TABLE

@Entity(tableName = MYDIARY_TABLE)
data class MyDiaryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @Embedded
    var myDiary: MyDiary
)
