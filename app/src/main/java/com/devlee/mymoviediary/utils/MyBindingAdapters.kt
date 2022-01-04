package com.devlee.mymoviediary.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.presentation.adapter.category.CategoryViewType
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
            setBackgroundColor(getColorRes(view.context, android.R.color.transparent))
            load(category.drawableRes)
        }
    } else {
        view.apply {
            if (category.color != null) {
                setBackgroundColor(category.color)
            } else {
                setBackgroundColor(getColorRes(view.context, android.R.color.transparent))
            }

            minimumWidth = 15.dp
            minimumHeight = 15.dp
        }
    }
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