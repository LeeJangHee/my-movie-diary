package com.devlee.mymoviediary.presentation.layout

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.ProgressBar

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
        val progress = ProgressBar(context)
        addContentView(progress, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
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