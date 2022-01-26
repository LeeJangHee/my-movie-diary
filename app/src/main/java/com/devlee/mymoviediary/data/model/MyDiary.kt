package com.devlee.mymoviediary.data.model

data class MyDiary(
    val date: String,                   // 날짜
    val contents: String? = null,       // 일기 내용
    val video: String? = null,          // 비디오 파일
    val recording: String? = null,      // 음성 파일
    val category: Category? = null,     // 카테고리
    val star: Boolean = false,          // 즐겨찾기
    val mood: Int? = null               // 기분 이미지
)
