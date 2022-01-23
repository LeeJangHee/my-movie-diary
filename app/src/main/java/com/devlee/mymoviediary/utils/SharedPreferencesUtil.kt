package com.devlee.mymoviediary.utils

import android.content.Context
import android.content.SharedPreferences
import com.devlee.mymoviediary.data.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

object SharedPreferencesUtil {

    private val PREF_NAME = "SharedPreferencesUtil"
    private val CATEGORY_KEY = "categoryKey"


    private lateinit var pref: SharedPreferences
    private val gson = Gson()


    fun init(context: Context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setCategoryListPref(categoryList: List<Category>) {
        val edit = pref.edit()
        val jsonArray = JSONArray()
        categoryList.forEach {
            jsonArray.put(gson.toJson(it))
        }
        if (categoryList.isNullOrEmpty()) {
            edit.putString(CATEGORY_KEY, null)
        } else {
            edit.putString(CATEGORY_KEY, jsonArray.toString())
        }
        edit.apply()
    }

    fun getCategoryListPref(): List<Category> {
        val json = pref.getString(CATEGORY_KEY, null)
        val list = mutableListOf<Category>()
        if (json != null) {
            try {
                val typeToken = object : TypeToken<Category>() {}.type
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val c: Category = gson.fromJson(a.optString(i), typeToken)
                    list.add(c)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return list
    }
}