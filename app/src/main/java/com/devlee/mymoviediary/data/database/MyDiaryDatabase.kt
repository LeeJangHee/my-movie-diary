package com.devlee.mymoviediary.data.database

import android.content.Context
import androidx.room.*
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.database.entity.MyDiaryEntity
import com.devlee.mymoviediary.utils.Constants.MYDIARY_DB_NAME

@Database(
    entities = [
        MyDiaryEntity::class,
        CategoryEntity::class
    ],
    version = 1
)
@TypeConverters(MyDiaryConverter::class)
abstract class MyDiaryDatabase : RoomDatabase() {

    abstract fun dao(): MyDiaryDao

    companion object {
        @Volatile
        private var INSTANCE: MyDiaryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): MyDiaryDatabase {
            return synchronized(this) {
                INSTANCE ?: createDatabase(context).also {
                    INSTANCE = it
                }
            }
        }


        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyDiaryDatabase::class.java,
                MYDIARY_DB_NAME
            ).build()
    }
}