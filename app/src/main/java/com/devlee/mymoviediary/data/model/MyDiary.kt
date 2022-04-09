package com.devlee.mymoviediary.data.model

import androidx.annotation.DrawableRes
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.utils.Constants.MEDIA_PREFIX

data class MyDiary(
    val date: String,                   // 날짜
    val contents: String? = null,       // 일기 내용
    val video: List<String?> = listOf(),          // 비디오 파일
    val recording: List<String?> = listOf(),      // 음성 파일
    val star: Boolean = false,          // 즐겨찾기
    @DrawableRes
    val mood: Int? = Mood.NONE.resId    // 기분 이미지
)

enum class Mood(@DrawableRes val resId: Int?) {
    NONE(null),
    ANGRY(R.drawable.angry_e_icon),
    SAD(R.drawable.sad_e_icon),
    FINE(R.drawable.fine_e_icon),
    GOOD(R.drawable.good_e_icon),
    HAPPY(R.drawable.happy_e_icon)
}
