package com.devlee.mymoviediary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDiaryDao {

    /** MyDairy */
    @Query("SELECT * FROM mydiary_table")
    fun getMyDiaryAll(): Flow<List<MyDiaryEntity>>

    @Query("SELECT * FROM mydiary_table WHERE contents LIKE :contents")
    fun searchMyDiary(contents: String?): Flow<List<MyDiaryEntity>>

    @Query("SELECT category_column FROM category_table WHERE categoryId = :categoryId")
    fun getMyDiaryByCategory(categoryId: Int?): Category?

    @Query("SELECT * FROM mydiary_table WHERE id = :myDiaryId")
    fun getMyDiaryById(myDiaryId: Int): MyDiary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyDiary(myDiaryEntity: MyDiaryEntity)

    @Query("DELETE FROM mydiary_table WHERE categoryEntityId = :categoryEntityId")
    suspend fun deleteMyDiaryByCategoryId(categoryEntityId: Int)

    @Query("DELETE FROM mydiary_table WHERE id = :id")
    suspend fun deleteMyDiary(id: Int)

    @Query("UPDATE mydiary_table SET " +
            "categoryEntityId = :categoryId, " +
            "date = :date,"+
            "contents = :contents, " +
            "video = :video, " +
            "recording = :recording, " +
            "star = :star, " +
            "mood = :mood " +
            "WHERE id = :id")
    suspend fun updateMyDiary(id: Int, categoryId: Int?, date: String, contents: String?, video: List<String?>, recording: List<String?>, star: Boolean?, mood: Int?)

    @Query("SELECT id FROM mydiary_table " +
            "WHERE date = :date AND (contents = :contents OR contents IS NULL) AND video = :video AND recording = :recording AND star = :star AND (mood = :mood OR mood IS NULL)")
    fun getMyDiaryId(date: String, contents: String?, video: List<String?>, recording: List<String?>, star: Boolean?, mood: Int?): Int?

    /** Category */
    @Query("SELECT * FROM category_table")
    fun getCategoryAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category_table WHERE title LIKE :title")
    fun searchCategory(title: String?): Flow<List<CategoryEntity>>

    @Query("SELECT DISTINCT count(*) AS count FROM mydiary_table " +
            "WHERE categoryEntityId = " +
            "(SELECT categoryId FROM category_table WHERE category_column = :category)")
    fun getCategoryCount(category: Category): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM category_table WHERE category_column = :category")
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE category_table SET category_column = :category, title = :title WHERE category_column = :preCategory")
    suspend fun updateCategory(category: Category, preCategory: Category, title: String)

    @Query("SELECT categoryId FROM category_table WHERE category_column = :category")
    fun getCategoryId(category: Category?): Int?
}