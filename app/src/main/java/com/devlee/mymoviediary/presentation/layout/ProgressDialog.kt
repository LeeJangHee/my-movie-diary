package com.devlee.mymoviediary.presentation.layout

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.ProgressBar
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.utils.getDrawableRes

class ProgressDialog(context: Context) : Dialog(context) {

    private val progressBar: ProgressBar = ProgressBar(context).apply {
        indeterminateDrawable = getDrawableRes(context = context, drawable = R.drawable.loading_anim_icon)
    }

    init {
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addContentView(progressBar, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    fun showProgress() {
        val context = context

        if (context is Activity) {
            if ((context).isFinishing) {
                return
            }
        }
        try {
            if (!isShowing) {
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}