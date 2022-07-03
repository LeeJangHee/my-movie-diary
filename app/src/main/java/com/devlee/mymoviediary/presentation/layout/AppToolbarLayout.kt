package com.devlee.mymoviediary.presentation.layout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.LayoutAppbarBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarSearchBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarTitleBinding
import com.devlee.mymoviediary.utils.dp
import com.devlee.mymoviediary.utils.gone
import com.devlee.mymoviediary.utils.show

class AppToolbarLayout(
    private val context: Context,
    private val binding: LayoutAppbarBinding
) {
    companion object {
        const val RIGHT = 0
        const val LEFT = 1
    }

    private var leftMenu: ArrayDeque<View> = ArrayDeque()
    private var rightMenu: ArrayDeque<View> = ArrayDeque()

    private val appbarTitleBinding: LayoutAppbarTitleBinding by lazy {
        LayoutAppbarTitleBinding.inflate(LayoutInflater.from(context))
    }

    private val appbarSearchBinding: LayoutAppbarSearchBinding by lazy {
        LayoutAppbarSearchBinding.inflate(LayoutInflater.from(context))
    }

    private var titleView: View? = null
    private var searchView: View? = null

    /** 검색 메뉴 */
    fun setSearchView(
        editorActionListener: TextView.OnEditorActionListener? = null,
        removeListener: View.OnClickListener? = null
    ) {
        searchView = appbarSearchBinding.run {
            searchEdit.text = null
            val lp = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 36.dp()).apply {
                setMargins(0, 0, 16.dp(), 0)
            }
            searchEdit.setOnEditorActionListener(editorActionListener)
            val removeButtonListener = removeListener ?: View.OnClickListener {
                searchEdit.text = null
            }
            this.removeListener = removeButtonListener
            root.apply {
                layoutParams = lp
                setPadding(10.dp(), 0, 10.dp(), 0)
            }
        }

        if (binding.layoutTitle.childCount > 0) {
            binding.layoutTitle.removeAllViews()
        }
        binding.layoutTitle.addView(searchView)
    }

    /** 타이틀 메뉴 */
    fun setTitleView(
        title: String,
        subTitle: String? = null,
        leftImage: Int? = null,
        rightImage: Int? = null,
        onClickListener: View.OnClickListener? = null
    ) {
        titleView = appbarTitleBinding.run {
            this.leftImage = leftImage
            this.rightImage = rightImage
            this.title = title
            this.subTitle = subTitle
            appbarTitleView.setOnClickListener(onClickListener)
            root
        }
        if (binding.layoutRightMenu.childCount > 0) {
            binding.layoutRightMenu.removeAllViews()
            rightMenu.clear()
        }
        if (binding.layoutLeftMenu.childCount > 0) {
            binding.layoutLeftMenu.removeAllViews()
            leftMenu.clear()
        }
        if (binding.layoutTitle.childCount > 0) {
            binding.layoutTitle.removeAllViews()
        }
        binding.layoutTitle.addView(titleView)
    }

    /** image menu */
    fun setImageOrTextMenu(type: Int, @DrawableRes resId: Int? = null, @StringRes strId: Int? = null, onClickListener: View.OnClickListener? = null) {
        val paddingHorizontal: Int = 16.dp()
        val paddingVertical: Int = 16.dp()
        var view = View(context)
        when {
            resId != null -> {
                view = ImageView(context).apply {
                    load(resId)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
                    setOnClickListener(onClickListener)
                }
            }
            strId != null -> {
                view = TextView(context).apply {
                    setText(strId)
                    setTextColor(context.getColor(R.color.color_1c1c1c))
                    textSize = 15f
                    setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
                    setOnClickListener(onClickListener)
                }
            }
        }

        if (type == LEFT) {
            setLeftMenuView(view)
        } else {
            setRightMenuView(view)
        }
    }

    fun setCustomView(view: View, type: Int) {

        if (type == LEFT) {
            setLeftMenuView(view)
        } else {
            setRightMenuView(view)
        }
    }

    private fun setLeftMenuView(view: View) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.applyMenu(lp)

        binding.layoutLeftMenu.addView(view)
        leftMenu.add(view)
    }

    private fun setRightMenuView(view: View) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.applyMenu(lp)

        binding.layoutRightMenu.addView(view)
        rightMenu.add(view)
    }

    /** Menu - LinearLayout set */
    private fun View.applyMenu(lp: LinearLayout.LayoutParams) = apply {
        layoutParams = lp
    }

    fun gone() {
        binding.root.gone()
    }

    fun show() {
        binding.root.show()
    }

    fun clearView() {
        binding.layoutRightMenu.removeAllViews()
        binding.layoutLeftMenu.removeAllViews()
        if (binding.layoutTitle.childCount > 0)
            binding.layoutTitle.removeAllViews()
        rightMenu.clear()
        leftMenu.clear()
    }


}