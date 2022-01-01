package com.devlee.mymoviediary.data.model

data class MyDiary(
    val date: String,
    val contents: String? = null,
    val video: String? = null,
    val recording: String? = null,
    val category: Category? = null,
    val star: Boolean = false
)
