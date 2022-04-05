package com.devlee.mymoviediary.utils.recyclerview

import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.presentation.adapter.category.MainCategoryAdapter
import com.devlee.mymoviediary.utils.AnimationUtil
import com.devlee.mymoviediary.utils.gone
import com.devlee.mymoviediary.utils.show
import kotlin.math.max
import kotlin.math.min

class CategoryTouchCallback : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    private val TAG = "CategoryTouchCallback"


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        if (getView(viewHolder) != null) {
            getDefaultUIUtil().clearView(getView(viewHolder))
        }
        previousPosition = viewHolder.bindingAdapterPosition
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            if (view != null) {
                val isClamped = getTag(viewHolder)
                val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)

                getSwipeView(viewHolder).changeVisibleByClamp(x)

                currentDx = x
                getDefaultUIUtil().onDraw(c, recyclerView, view, x, dY, actionState, isCurrentlyActive)
            }
        }
        Log.w(TAG, "actionState = $actionState\n ACTION_STATE_IDLE = 0 | ACTION_STATE_SWIPE = 1 | ACTION_STATE_DRAG = 2 ")
        Log.v(TAG, "onChildDraw() viewHolder = $viewHolder, dX = $dX, dY = $dY, currentDx = $currentDx")
    }

    private fun clampViewPositionHorizontal(
        view: View,
        dx: Float,
        isClamped: Boolean,
        isCurrentActive: Boolean
    ): Float {
        val min: Float = -view.width.toFloat() / 2
        val max: Float = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentActive) dx - clamp else -clamp
        } else {
            dx
        }
        return min(max(min, x), max)
    }

    private fun View?.changeVisibleByClamp(x: Float) {
        this ?: return

        if (x == 0f) gone()
        else show()
        requestLayout()
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View? {
        return when (viewHolder) {
            is MainCategoryAdapter.CategoryHolder -> viewHolder.itemView.findViewById(R.id.categoryItem)
            else -> null
        }
    }

    private fun getSwipeView(viewHolder: RecyclerView.ViewHolder): View? {
        return when (viewHolder) {
            is MainCategoryAdapter.CategoryHolder -> viewHolder.itemView.findViewById(R.id.categorySwipeView)
            else -> null
        }
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    fun removePreviousClamp(recyclerView: RecyclerView) {
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            removePreviousClamp(viewHolder)
        }
    }

    fun removePreviousClamp(viewHolder: RecyclerView.ViewHolder) {
        if (currentPosition == previousPosition) return

        Log.i(TAG, "removePreviousClamp: previousPosition = $previousPosition")

        val fadeOutAnim = AnimationUtil.fadeOut(viewHolder.itemView.context)
        fadeOutAnim.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    getSwipeView(viewHolder)?.gone()
                }
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
            }
        )

        previousPosition?.let {
            getSwipeView(viewHolder)?.startAnimation(fadeOutAnim)
            getView(viewHolder)?.animate()?.translationX(0f)
            setTag(viewHolder, false)
            previousPosition = null
        }
    }
}

// https://velog.io/@trycatch98/Android-RecyclerView-Swipe-Menu