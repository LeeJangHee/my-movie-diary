package com.devlee.mymoviediary.utils

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.presentation.adapter.category.CategoryViewType
import com.devlee.mymoviediary.presentation.adapter.category.FirstColorPickAdapter
import com.devlee.mymoviediary.utils.recyclerview.CategoryDecoration
import com.devlee.mymoviediary.utils.recyclerview.CustomDecoration

@BindingAdapter("bindImage")
fun bindSetImage(view: ImageView, drawable: Int?) {
    if (drawable != null) {
        view.show()
        view.load(drawable)
    } else {
        view.gone()
    }
}

@BindingAdapter("setSubTitle")
fun bindSetSubTitle(view: AppCompatTextView, subtitle: String?) {
    view.text = subtitle
    view.visibility = if (subtitle != null) View.VISIBLE else View.GONE
}

@BindingAdapter(
    value = ["divHeight", "divPadding", "divColor", "divType"],
    requireAll = false
)
fun RecyclerView.setDivider(divHeight: Float?, divPadding: Float?, @ColorInt divColor: Int?, divType: Int = 0) {
    val decoration = when (divType) {
        1 -> {
            CategoryDecoration(
                height = divHeight ?: 0f,
                padding = divPadding ?: 0f,
                color = divColor ?: Color.TRANSPARENT
            )
        }
        else -> {
            CustomDecoration(
                height = divHeight ?: 0f,
                padding = divPadding ?: 0f,
                color = divColor ?: Color.TRANSPARENT
            )
        }
    }
    addItemDecoration(decoration)
}

@BindingAdapter("categoryImages")
fun bindCategoryHeaderImage(view: ImageView, category: Category) {
    if (category.drawableRes != null) {
        view.apply {
            setBackgroundColor(getColorRes(context, android.R.color.transparent))
            load(category.drawableRes)
            show()
        }
    } else {
        view.gone()
    }
}

@BindingAdapter("categoryColor")
fun bindCategoryHeaderColor(view: ImageView, @ColorInt colorRes: Int?) {
    colorRes?.let {
        view.apply {
            setBackgroundColor(it)
            show()
        }
    } ?: view.gone()
}

@BindingAdapter("categoryTextStyle")
fun bindCategoryTextStyle(view: TextView, category: Category) {
    when (category.type) {
        CategoryViewType.ADD.type -> {
            view.setTextColor(getColorRes(view.context, R.color.color_999999))
        }
        else -> {
            view.setTextColor(getColorRes(view.context, R.color.color_1c1c1c))
        }
    }
}

@BindingAdapter("categoryEdit")
fun EditText.setAddMode(mode: Boolean) {
    if (mode) {
        this.apply {
            show()
            requestFocus()
            showKeyboardIME(this)
        }
    } else {
        this.apply {
            gone()
            clearFocus()
            hideKeyboardIME()
        }
    }
}

@BindingAdapter("firstColorAdapter")
fun RecyclerView.setFirstColorAdapter(adapter: FirstColorPickAdapter) {
    val colorList = arrayListOf(
        getColorRes(this.context, R.color.all_pick_0),
        getColorRes(this.context, R.color.all_pick_1),
        getColorRes(this.context, R.color.all_pick_2),
        getColorRes(this.context, R.color.all_pick_3),
        getColorRes(this.context, R.color.all_pick_4),
        getColorRes(this.context, R.color.all_pick_5),
        getColorRes(this.context, R.color.all_pick_6),
        getColorRes(this.context, R.color.all_pick_7),
        getColorRes(this.context, R.color.all_pick_8),
        getColorRes(this.context, R.color.all_pick_9),
        getColorRes(this.context, R.color.all_pick_10),
        getColorRes(this.context, R.color.all_pick_11),
        getColorRes(this.context, R.color.all_pick_12),
        getColorRes(this.context, R.color.all_pick_13),
        getColorRes(this.context, R.color.all_pick_14)
    )
    this.apply {
        this.adapter = adapter.apply {
            // 아이템 set
            setColorIdList(colorList)

            // 아이템 아래 간격
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val bottom = 26f.convertDpToPx()
                    if (parent.getChildAdapterPosition(view) < 10) {
                        outRect.bottom = bottom
                    }
                }
            })
        }
        layoutManager = GridLayoutManager(context, 5)
    }
}