package com.devlee.mymoviediary.data.repository

import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.database.entity.CategoryEntity

class MyDiaryRepository(
    private val db: MyDiaryDatabase
) {
    /** MyDiary Database */
//    fun getMyDiaryAll() = db.dao().getMyDiaryAll()

    /** Category Database */
    fun getCategoryAll() = db.dao().getCategoryAll()

//    suspend fun insertCategory(title: String, type: Int, color: Int) = db.dao().insertCategory(title, type, color)
//
//    suspend fun deleteCategory(categoryEntity: CategoryEntity) = db.dao().deleteCategory(categoryEntity)
}