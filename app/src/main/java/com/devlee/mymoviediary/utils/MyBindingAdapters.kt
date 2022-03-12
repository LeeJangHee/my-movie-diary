package com.devlee.mymoviediary.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.fetch.VideoFrameUriFetcher
import coil.load
import coil.request.videoFrameMillis
import coil.size.Scale
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.Mood
import com.devlee.mymoviediary.domain.use_case.ContentChoiceData
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

@BindingAdapter("contentChoiceItem")
fun View.setContentChoiceItem(data: ContentChoiceData) {
    var title: String? = null
    var time: String? = null
    var isAudio = false

    data.video?.let { videoUri ->
        context.contentResolver.query(videoUri, FileUtil.getVideoProjection(), null, null, null)?.use { cursor ->
            val durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
            cursor.moveToFirst()
            time = DateFormatUtil.getAudioTimeLine(cursor.getString(durationIndex).toLong())
        }
    }

    data.audio?.let { audioUri ->
        isAudio = true
        context.contentResolver.query(audioUri, FileUtil.getAudioProjection(), null, null, null)?.use { cursor ->
            val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            cursor.moveToFirst()
            title = cursor.getString(titleIndex)
            time = DateFormatUtil.getAudioTimeLine(cursor.getString(durationIndex).toLong())
        }
    }

    when (this) {
        is ImageView -> {
            if (isAudio) {
                load(R.drawable.content_item_audio_icon) {
                    scale(Scale.FILL)
                }
            } else {
                scaleType = ImageView.ScaleType.CENTER_CROP
                load(data.video) {
                    fetcher(VideoFrameUriFetcher(context))
                    videoFrameMillis(0)
                }
            }
        }
        is TextView -> {
            when (id) {
                R.id.itemCreateAudioTitle -> {
                    text = title
                }
                R.id.itemCreateTimeText -> {
                    if (isAudio) {
                        setTextColor(getColorRes(context, R.color.color_1c1c1c))
                        background = null
                    } else {
                        setTextColor(getColorRes(context, R.color.white))
                        background = MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply {
                            setAllCorners(CornerFamily.ROUNDED, 4.toDp())
                        }.build()).apply {
                            fillColor = ColorStateList.valueOf(getColorRes(context, R.color.color_99000000))
                        }
                    }
                    text = time
                }
            }
        }
    }
}

@BindingAdapter("allCornerSize", "setBackgroundColor", requireAll = false)
fun View.setAllCorner(size: Int, @ColorRes color: Int = android.R.color.transparent) {
    val model = ShapeAppearanceModel().toBuilder().apply {
        setAllCorners(CornerFamily.ROUNDED, size.toDp())
    }.build()

    background = MaterialShapeDrawable(model).apply {
        fillColor = ColorStateList.valueOf(getColorRes(context, color))
    }
}