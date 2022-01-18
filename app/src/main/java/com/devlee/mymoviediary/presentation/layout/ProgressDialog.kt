package com.devlee.mymoviediary.presentation.layout

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import coil.load
import com.devlee.mymoviediary.R

class ProgressDialog(context: Context) : Dialog(context) {

    private val anim = AnimationUtils.loadAnimation(context, R.anim.loading_anim)
    private val imageView = ImageView(context).apply {
        load(R.drawable.loading_icon)
    }

    init {
        setCancelable(false)
        addContentView(imageView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    fun showProgress() {
        val context = context

        if (context is Activity) {
            if ((context as Activity).isFinishing) {
                return
            }
        }
        try {
            if (!isShowing) {
                imageView.startAnimation(anim)
                show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissProgress() {
        try {
            if (isShowing) {
                dismiss()
                imageView.clearAnimation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}