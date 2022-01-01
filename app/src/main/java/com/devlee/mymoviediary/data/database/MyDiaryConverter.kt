package com.devlee.mymoviediary.data.database

import androidx.room.TypeConverter
import com.devlee.mymoviediary.data.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyDiaryConverter {

    var gson = Gson()

    @TypeConverter
    fun toCategory(data: String): Category {
        val typeToken = object : TypeToken<Category>() {}.type
        return gson.fromJson(data, typeToken)
    }

    @TypeConverter
    fun fromCategory(category: Category): String {
        return gson.toJson(category)
    }
}