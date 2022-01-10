package com.devlee.mymoviediary.utils

import android.view.View
import androidx.lifecycle.MutableLiveData


/** 프로그레스바 라이브데이터 */
val loadingLiveData = MutableLiveData(false)

/** 처음 색 선택 클릭 리스너 */
var categoryFirstItemClick: ((Int?) -> Unit)? = null

var categoryErrorView: ((View) -> Unit)? = null