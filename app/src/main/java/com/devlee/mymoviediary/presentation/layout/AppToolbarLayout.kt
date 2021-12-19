package com.devlee.mymoviediary.presentation.layout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.devlee.mymoviediary.databinding.ActivityMainBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarBinding
import com.devlee.mymoviediary.utils.gone

class AppToolbarLayout(
    private val context: Context,
    private val binding: LayoutAppbarBinding
) {
    companion object {
        const val RIGHT = 0
        const val LEFT = 1
    }

    var leftMenu: ArrayList<View> = arrayListOf()
    var rightMenu: ArrayList<View> = arrayListOf()

    var layoutTitle: RelativeLayout? = null
    var layoutLeftMenu: LinearLayout? = null
    var layoutRightMenu: LinearLayout? = null

    var titleView: View? = null

    fun setTitle(@StringRes title: Int) {
        setTitle(context.getString(title))
    }

    fun setTitle(title: String) {
        if (titleView == null || titleView !is TextView) {
            titleView = TextView(context)

            // 레이아웃 위치 잡기
            val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.addRule(RelativeLayout.CENTER_IN_PARENT)

            titleView?.layoutParams = lp
            // 스타일 적용
//            (titleView as TextView).setTextAppearance(R.style.)
            layoutTitle?.addView(titleView)
        }
        (titleView as TextView).text = title
    }

    fun setImageTitle(@DrawableRes title: Int) {

    }

    fun gone() {
        binding.root.gone()
    }


}