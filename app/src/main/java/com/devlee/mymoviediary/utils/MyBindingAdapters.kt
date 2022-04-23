package com.devlee.mymoviediary.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.fetch.VideoFrameUriFetcher
import coil.load
import coil.request.videoFrameMillis
import coil.size.Scale
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.Mood
import com.devlee.mymoviediary.data.model.MyDiary
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
    value = ["divHeight", "divPaddingLeft", "divPaddingRight", "divColor", "divType"],
    requireAll = false
)
fun RecyclerView.setDivider(
    divHeight: Float?,
    divPaddingLeft: Float?,
    divPaddingRight: Float?,
    @ColorInt divColor: Int?,
    divType: Int = 0
) {
    val decoration = when (divType) {
        1 -> {
            CategoryDecoration(
                height = divHeight ?: 0f,
                paddingLeft = divPaddingLeft ?: 0f,
                paddingRight = divPaddingRight ?: 0f,
                color = divColor ?: Color.TRANSPARENT
            )
        }
        else -> {
            CustomDecoration(
                height = divHeight ?: 0f,
                paddingLeft = divPaddingLeft ?: 0f,
                paddingRight = divPaddingRight ?: 0f,
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

@BindingAdapter("categoryCountText")
fun TextView.setCategoryCount(categoryCount: Int?) {
    Log.e("MyBindingAdapter", "category count = $categoryCount")
    text = if (categoryCount != null) {
        if (categoryCount >= 1000) {
            context.getString(R.string.category_max_count_text)
        } else {
            categoryCount.toString()
        }
    } else {
        null
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

@BindingAdapter("videoThumbnail", "recordingImage", requireAll = false)
fun ImageView.uriThumbnail(videoUri: List<String?>?, audioUri: List<String?>?) {
    Log.i("janghee", "uriThumbnail-()")
    when (id) {
        R.id.videoImage, R.id.gridVideoImage -> {
            gone()
            if (!videoUri.isNullOrEmpty()) {
                (layoutParams as FrameLayout.LayoutParams).apply {
                    if (id == R.id.videoImage) {
                        width = 103.dp()
                        height = 103.dp()
                    }
                }
                videoUri.toUri().first()?.let {
                    show()
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    load(it) {
                        fetcher(VideoFrameUriFetcher(context))
                        videoFrameMillis(0)
                    }
                }
            }
        }
        R.id.audioImage, R.id.gridAudioImage -> {
            gone()
            if (!audioUri.isNullOrEmpty()) {
                (layoutParams as FrameLayout.LayoutParams).apply {
                    width = 24.dp()
                    height = 24.dp()
                }
                audioUri.toUri().first()?.let {
                    show()
                    load(R.drawable.content_item_audio_icon) {
                        scale(Scale.FILL)
                    }
                }
            }
        }
    }

}

@BindingAdapter("homeEmptyImage")
fun View.setHomeEmptyImage(myDiary: MyDiary) {
    if (myDiary.video.isNullOrEmpty() && myDiary.recording.isNullOrEmpty()) {
        show()
        Log.d("janghee", "setHomeLayoutVisible: show-()")
    } else {
        gone()
        Log.d("janghee", "setHomeLayoutVisible: gone-()")
    }

}

@BindingAdapter("pagingFooterState")
fun View.setPagingFooterState(isLoading: Boolean?) {
    when (this) {
        is ProgressBar -> {
            if (isLoading == true) show()
            else gone()
        }

        is Button -> {
            if (isLoading == false) show()
            else gone()
        }

    }
}

@BindingAdapter("diaryDetailGroupType")
fun Group.isVisibleFileType(myDiary: MyDiary?) {
    myDiary?.let {
        when (id) {
            R.id.diaryDetailVideoGroup -> {
                if (it.video.isNullOrEmpty()) {
                    gone()
                } else {
                    show()
                }
            }
            R.id.diaryDetailRadioGroup -> {
                if (it.recording.isNullOrEmpty()) {
                    gone()
                } else {
                    show()
                }
            }
        }
    } ?: gone()

}

@BindingAdapter("diaryDetailMood")
fun ImageView.setMood(@DrawableRes resId: Int?) {
    resId?.let {
        show()
        load(it)
    } ?: gone()
}