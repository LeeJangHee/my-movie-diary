package com.devlee.mymoviediary.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.devlee.mymoviediary.R

object AnimationUtil {

    fun downToUp(context: Context): Animation
        = AnimationUtils.loadAnimation(context, R.anim.down_to_up_arrow)

    fun upToDown(context: Context): Animation
        = AnimationUtils.loadAnimation(context, R.anim.up_to_down_arrow)

    fun fadeIn(context: Context): Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    fun fadeOut(context: Context): Animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
}