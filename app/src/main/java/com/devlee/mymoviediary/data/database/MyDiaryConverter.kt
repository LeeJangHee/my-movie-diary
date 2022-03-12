package com.devlee.mymoviediary.data.database

import androidx.room.TypeConverter
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyDiaryConverter {

    var gson = Gson()


    @TypeConverter
    fun toMyDiary(data: String): MyDiary {
        val typeToken = object : TypeToken<MyDiary>() {}.type
        return gson.fromJson(data, typeToken)
    }

    @TypeConverter
    fun fromCategory(myDiary: MyDiary): String {
        return gson.toJson(myDiary)
    }

    @TypeConverter
    fun toUriList(data: String?): List<String?> {
        val typeToken = object : TypeToken<List<String?>>() {}.type
        return gson.fromJson(data, typeToken)
    }

    @TypeConverter
    fun fromUriList(list: List<String?>): String? {
        return gson.toJson(list)
    }

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