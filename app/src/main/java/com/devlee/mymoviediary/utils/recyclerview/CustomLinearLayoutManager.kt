package com.devlee.mymoviediary.utils.recyclerview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLinearLayoutManager(
    context: Context,
    private val scrollable: Boolean,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
) : LinearLayoutManager(context, orientation, false) {
    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && scrollable
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && scrollable
    }
}