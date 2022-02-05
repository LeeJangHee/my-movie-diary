package com.devlee.mymoviediary.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import com.devlee.mymoviediary.presentation.activity.main.MainActivity

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

/** Convert px to dp */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Float.dp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Int.dp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Float.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Int.toDp(): Float = (this * Resources.getSystem().displayMetrics.density)

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
    if (set) {
        this.showBottomNav()
    } else {
        this.hideBottomNav()
    }
}

/** Animation */
fun View.startDownToUpAnimation() {
    startAnimation(AnimationUtils.downToUp(this.context))
}
fun View.startUpToDownAnimation() {
    startAnimation(AnimationUtils.upToDown(this.context))
}
fun View.startFadeInAnimation() {
    startAnimation(AnimationUtils.fadeIn(this.context))
}
fun View.startFadeOutAnimation() {
    startAnimation(AnimationUtils.fadeOut(this.context))
}