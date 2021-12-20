package com.devlee.mymoviediary.presentation.layout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.setPadding
import com.devlee.mymoviediary.databinding.ActivityMainBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarTitleBinding
import com.devlee.mymoviediary.utils.convertDpToPx
import com.devlee.mymoviediary.utils.gone
import java.util.*
import kotlin.collections.ArrayList

class AppToolbarLayout(
    private val context: Context,
    private val binding: LayoutAppbarBinding
) {
    companion object {
        const val RIGHT = 0
        const val LEFT = 1
    }

    var leftMenu: Queue<View> = LinkedList()
    var rightMenu: Queue<View> = LinkedList()

    var layoutTitle: RelativeLayout? = null

    private val appbarTitleBinding: LayoutAppbarTitleBinding by lazy {
        LayoutAppbarTitleBinding.inflate(LayoutInflater.from(context), binding.layoutTitle, true)
    }

    var titleView: View? = null

    /** 타이틀 메뉴 */
    fun setTitleView(
        title: String,
        leftImage: Int? = null,
        rightImage: Int? = null,
        leftClickListener: View.OnClickListener? = null,
        rightClickListener: View.OnClickListener? = null
    ) {
        titleView = appbarTitleBinding.run {
            this.leftImage = leftImage
            this.rightImage = rightImage
            this.title = title
            appbarTitleLeftImage.setOnClickListener(leftClickListener)
            appbarTitleRightImage.setOnClickListener(rightClickListener)
            root
        }
        if (binding.layoutRightMenu.childCount > 0){
            binding.layoutRightMenu.removeView(rightMenu.poll())
        }
        if (binding.layoutLeftMenu.childCount > 0) {
            binding.layoutLeftMenu.removeView(leftMenu.poll())
        }
        layoutTitle?.addView(titleView)
    }

    /** image menu */
    fun setImageMenu(type: Int, @DrawableRes resId: Int, onClickListener: View.OnClickListener? = null) {
        val padding: Int = 6f.convertDpToPx()
        val imageView = ImageView(context).apply {
            setImageResource(resId)
            setPadding(padding)
        }
        if (type == LEFT) {
            setLeftImageView(imageView, onClickListener)
        } else {
            setRightImageView(imageView, onClickListener)
        }
    }

    private fun setLeftImageView(view: View, onClickListener: View.OnClickListener?) {
        view.setOnClickListener(onClickListener)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = lp

        binding.layoutLeftMenu.addView(view,0)
        leftMenu.add(view)
    }

    private fun setRightImageView(view: View, onClickListener: View.OnClickListener?) {
        view.setOnClickListener(onClickListener)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = lp

        binding.layoutRightMenu.addView(view, 0)
        rightMenu.add(view)
    }

    fun gone() {
        binding.root.gone()
    }

    fun clearView() {
        binding.layoutRightMenu.removeAllViews()
        binding.layoutLeftMenu.removeAllViews()
        binding.layoutTitle.removeAllViews()
    }


}