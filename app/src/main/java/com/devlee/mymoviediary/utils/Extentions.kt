package com.devlee.mymoviediary.utils

import android.view.View

fun View.show() = run { visibility = View.VISIBLE }
fun View.gone() = run { visibility = View.GONE }
fun View.hide() = run { visibility = View.INVISIBLE }