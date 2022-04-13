package com.devlee.mymoviediary.data.repository

import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.data.model.Category
import kotlinx.coroutines.flow.Flow

class MyDiaryRepository(
    private val db: MyDiaryDatabase
) {
    /** MyDiary Database */
    fun getMyDiaryAll(): Flow<List<MyDiaryEntity>> = db.dao().getMyDiaryAll()

    fun searchMyDiary(contents: String?): Flow<List<MyDiaryEntity>> = db.dao().searchMyDiary(contents?.let { "%$contents%" })

    suspend fun insertMyDiary(myDiaryEntity: MyDiaryEntity) = db.dao().insertMyDiary(myDiaryEntity)

    fun getMyDiaryByCategory(categoryId: Int?): Category? = db.dao().getMyDiaryByCategory(categoryId)

    suspend fun deleteMyDiaryByCategoryId(categoryEntityId: Int) {
        db.dao().deleteMyDiaryByCategoryId(categoryEntityId)
    }

    suspend fun deleteMyDiary(id: Int) {
        db.dao().deleteMyDiary(id)
    }

    /** Category Database */
    fun getCategoryAll(): Flow<List<CategoryEntity>> = db.dao().getCategoryAll()

    fun searchCategory(title: String?): Flow<List<CategoryEntity>> = db.dao().searchCategory(title?.let { "%$title%" })

    fun getCategoryCount(category: Category): Int = db.dao().getCategoryCount(category)

    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        db.dao().insertCategory(categoryEntity)
    }

    suspend fun deleteCategory(category: Category) {
        db.dao().deleteCategory(category)
    }

    suspend fun updateCategory(category: Category, preCategory: Category, title: String) {
        db.dao().updateCategory(category, preCategory, title)
    }

    fun getCategoryId(category: Category?): Int? = db.dao().getCategoryId(category)
}