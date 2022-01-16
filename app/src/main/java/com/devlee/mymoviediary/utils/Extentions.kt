package com.devlee.mymoviediary.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/** View visibility */
fun View.show(isShow: Boolean? = null) {
    if (isShow == false) this.visibility = View.GONE
    else this.visibility = View.VISIBLE
}
fun View.gone() = run { visibility = View.GONE }
fun View.hide() = run { visibility = View.INVISIBLE }

/** xml size convert */
fun Float.convertDpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()
fun Float.convertSpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics).toInt()
fun Float.convertPxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Context.convertDpToDimen(dimenRes: Int) = (this.resources.getDimension(dimenRes) / this.resources.displayMetrics.density).toInt()
fun Float.getDimension(): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics).toInt()

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

fun MainActivity.isBottomNav(set: Boolean) {
    val bottomNavView = this.findViewById<BottomNavigationView>(R.id.mainBottomNav)
    if (set) {
        bottomNavView.show()
    } else {
        bottomNavView.gone()
    }
}