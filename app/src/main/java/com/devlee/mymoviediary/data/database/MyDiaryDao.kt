package com.devlee.mymoviediary.data.database

import androidx.room.*
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.utils.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDiaryDao {

    /** MyDairy */
    @Query("SELECT * FROM mydiary_table")
    fun getMyDiaryAll(): Flow<List<MyDiaryEntity>>

    /** Category */
    @Query("SELECT * FROM category_table")
    fun getCategoryAll(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM category_table WHERE category = :category")
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE category_table SET category = :category WHERE category = :preCategory")
    suspend fun updateCategory(category: Category, preCategory: Category)

}