package com.devlee.mymoviediary.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/** View visibility */
fun View.show() = run { visibility = View.VISIBLE }
fun View.gone() = run { visibility = View.GONE }
fun View.hide() = run { visibility = View.INVISIBLE }

/** xml size convert */
fun Float.convertDpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()
fun Float.convertSpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics).toInt()
fun Float.convertPxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


fun getColorRes(context: Context, @ColorRes color: Int): Int {
    return ContextCompat.getColor(context, color)
}

/** 키보드 제어 */
fun View.showKeyboardIME(editText: EditText) {
    editText.requestFocus()
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(editText, 0)
}

fun View.hideKeyboardIME() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}