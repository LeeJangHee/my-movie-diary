package com.devlee.mymoviediary.utils

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.devlee.mymoviediary.data.model.Category


/** 프로그레스바 라이브데이터 */
val loadingLiveData = MutableLiveData(false)

/** 처음 색 선택 클릭 리스너 */
var categoryFirstItemClick: ((Int?) -> Unit)? = null
var categoryUserPickItemClick: (() -> Unit)? = null

var categoryErrorView: ((View) -> Unit)? = null
var selectedCategoryCallback: ((Category) -> Unit)? = null

var isMainBottomNavLayout = MutableLiveData(true)