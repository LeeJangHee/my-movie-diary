package com.devlee.mymoviediary.utils.recyclerview

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.presentation.adapter.category.MainCategoryAdapter

class CategoryDecoration(
    private val height: Float,
    private val paddingLeft: Float,
    private val paddingRight: Float,
    @ColorInt private val color: Int
) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingStart + paddingLeft
        val right = parent.width - parent.paddingEnd - paddingRight

        val itemCount = parent.childCount

        for (i in 0 until itemCount - 1) {
            val child = parent.getChildAt(i)

            val pos = parent.getChildAdapterPosition(child)
            val viewHolder = parent.getChildViewHolder(child)
            if (viewHolder is MainCategoryAdapter.DefaultHolder) {
                val nextChild = parent.getChildAt(pos + 1)
                val nextViewHolder = parent.getChildViewHolder(nextChild)
                if (nextViewHolder !is MainCategoryAdapter.DefaultHolder) {

                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val top = (child.bottom + params.bottomMargin).toFloat()
                    val bottom = top + height

                    c.drawRect(left, top, right, bottom, paint)
                }
            }
        }
    }
}