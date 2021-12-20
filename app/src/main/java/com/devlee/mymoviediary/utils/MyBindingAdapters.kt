package com.devlee.mymoviediary.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("bindImage")
fun bindSetImage(view: ImageView, drawable: Int?) {
    if (drawable != null) {
        view.show()
        view.load(drawable)
    } else {
        view.gone()
    }
}