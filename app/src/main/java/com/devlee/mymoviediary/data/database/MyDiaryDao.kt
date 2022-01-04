package com.devlee.mymoviediary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.utils.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDiaryDao {

    /** MyDairy */
//    @Query("SELECT * FROM mydiary_table")
//    fun getMyDiaryAll(): Flow<List<MyDiaryEntity>>

    /** Category */
    @Query("SELECT * FROM category_table")
    fun getCategoryAll(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity)
//
//    @Delete
//    suspend fun deleteCategory(categoryEntity: CategoryEntity)


}