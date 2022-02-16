package com.devlee.mymoviediary.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.Mood
import com.devlee.mymoviediary.presentation.adapter.category.CategoryViewType
import com.devlee.mymoviediary.utils.recyclerview.CategoryDecoration
import com.devlee.mymoviediary.utils.recyclerview.CustomDecoration
import com.devlee.mymoviediary.viewmodels.SortItem
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

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

@BindingAdapter("moodButton")
fun ImageButton.setMood(mood: Mood) = run {
    Log.d("moodRadioButton", "setMood: ${mood.name}")

    when (mood) {
        Mood.NONE -> {
            isSelected = id == R.id.moodNone
        }
        Mood.SAD -> {
            isSelected = id == R.id.moodSad
        }
        Mood.ANGRY -> {
            isSelected = id == R.id.moodAngry
        }
        Mood.FINE -> {
            isSelected = id == R.id.moodFine
        }
        Mood.GOOD -> {
            isSelected = id == R.id.moodGood
        }
        Mood.HAPPY -> {
            isSelected = id == R.id.moodHappy
        }
    }
    var w: Int = resources.getDimension(R.dimen.mood_collapse_width).toInt()
    var h: Int = resources.getDimension(R.dimen.mood_collapse_height).toInt()
    if (isSelected && id != R.id.moodNone) {
        w = resources.getDimension(R.dimen.mood_expend_width).toInt()
        h = resources.getDimension(R.dimen.mood_expend_height).toInt()
    }
    layoutParams.apply {
        width = w
        height = h
        requestLayout()
    }
}

@BindingAdapter("sortItemText")
fun TextView.setSortItem(item: SortItem?) {
    text = item?.title

    when (id) {
        R.id.sortPopupMenu -> {
            val modelPopupMenu = ShapeAppearanceModel().toBuilder().apply {
                setBottomLeftCorner(CornerFamily.ROUNDED, 4.toDp())
                setBottomRightCorner(CornerFamily.ROUNDED, 4.toDp())
            }.build()

            val shapePopupMenu = MaterialShapeDrawable(modelPopupMenu).apply {
                val backgroundColor = getColorRes(context, R.color.white)
                fillColor = ColorStateList.valueOf(backgroundColor)
            }
            background = shapePopupMenu
        }
    }
}
