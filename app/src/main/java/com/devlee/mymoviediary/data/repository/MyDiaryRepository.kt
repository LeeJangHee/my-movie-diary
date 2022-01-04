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

    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        db.dao().insertCategory(categoryEntity)
    }
//
//    suspend fun deleteCategory(categoryEntity: CategoryEntity) = db.dao().deleteCategory(categoryEntity)
}