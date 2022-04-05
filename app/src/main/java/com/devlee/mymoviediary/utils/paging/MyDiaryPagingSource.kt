package com.devlee.mymoviediary.utils.paging

import com.devlee.mymoviediary.data.model.MyDiary

class MyDiaryPagingSource(
    override val items: List<MyDiary>,
    override val pageSize: Int
): BasePagingSource<MyDiary>() {

}