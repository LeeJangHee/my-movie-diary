package com.devlee.mymoviediary.data.database

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.compose.ui.state.ToggleableState
import androidx.room.*
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDiaryDao {

    /** MyDairy */
//    @Query("SELECT * FROM mydiary_table")
//    fun getMyDiaryAll(): Flow<List<MyDiaryEntity>>

    /** Category */
    @Query("SELECT * FROM category_table")
    fun getCategoryAll(): Flow<List<CategoryEntity>>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCategory(
//        title: String,
//        type: Int,
//        @ColorInt color: Int? = null,
//        @DrawableRes drawableRes: Int? = null
//    )
//
//    @Delete
//    suspend fun deleteCategory(categoryEntity: CategoryEntity)


}