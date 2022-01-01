package com.devlee.mymoviediary.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devlee.mymoviediary.data.model.Category

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
    value = ["divHeight", "divPadding", "divColor"],
    requireAll = false
)
fun RecyclerView.setDivider(divHeight: Float?, divPadding: Float?, @ColorInt divColor: Int?) {
    val decoration = CustomDecoration(
        height = divHeight ?: 0f,
        padding = divPadding ?: 0f,
        color = divColor ?: Color.TRANSPARENT
    )
    addItemDecoration(decoration)
}

@BindingAdapter("categoryImages")
fun bindCategoryHeaderImage(view: ImageView, category: Category) {
    if (category.drawableRes != null) {
        view.apply {
            setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
            load(category.drawableRes)
        }
    } else {
        view.apply {
            setBackgroundColor(ContextCompat.getColor(view.context, category.color ?: android.R.color.transparent))
            minimumWidth = 15.dp
            minimumHeight = 15.dp
        }
    }
}