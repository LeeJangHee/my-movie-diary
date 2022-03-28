package com.devlee.mymoviediary.data.repository

import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.data.model.Category

class MyDiaryRepository(
    private val db: MyDiaryDatabase
) {
    /** MyDiary Database */
    fun getMyDiaryAll() = db.dao().getMyDiaryAll()

    fun searchMyDiary(contents: String?) = db.dao().searchMyDiary(contents?.let { "%$contents%" })

    suspend fun insertMyDiary(myDiaryEntity: MyDiaryEntity) = db.dao().insertMyDiary(myDiaryEntity)

    /** Category Database */
    fun getCategoryAll() = db.dao().getCategoryAll()

    fun searchCategory(title: String?) = db.dao().searchCategory(title?.let { "%$title%" })

    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        db.dao().insertCategory(categoryEntity)
    }

    suspend fun deleteCategory(category: Category) {
        db.dao().deleteCategory(category)
    }

    suspend fun updateCategory(category: Category, preCategory: Category) {
        db.dao().updateCategory(category, preCategory)
    }
}