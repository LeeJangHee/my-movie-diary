package com.devlee.mymoviediary.presentation.layout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.utils.getColorRes
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class RoundDialog @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyle) {

    private var cornerRadius: Int
    private var cornerRadiusTop: Int

    init {
        context.obtainStyledAttributes(attrs, R.styleable.RoundDialog, defStyleAttr, defStyle).apply {
            try {
                cornerRadius = getDimensionPixelSize(R.styleable.RoundDialog_cornerRadius, 0)
                cornerRadiusTop = getDimensionPixelSize(R.styleable.RoundDialog_cornerRadiusTop, 0)

                if (cornerRadius > 0) {
                    setCornerRadius()
                }

                if (cornerRadiusTop > 0) {
                    setCornerRadiusTop()
                }
            } finally {
                recycle()
            }
        }
    }

    private fun setCornerRadius() {
        val background = background
        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadius = cornerRadius.toFloat()

        if (background is ColorDrawable) {
            gradientDrawable.setColor(background.color)
        }
        setBackground(gradientDrawable)
        clipToOutline = true
    }

    private fun setCornerRadiusTop() {
        clipToOutline = true
        val model = ShapeAppearanceModel().toBuilder().apply {
            setTopRightCorner(CornerFamily.ROUNDED, cornerRadiusTop.toFloat())
            setTopLeftCorner(CornerFamily.ROUNDED, cornerRadiusTop.toFloat())
        }.build()

        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = getColorRes(context, R.color.white)
            fillColor = ColorStateList.valueOf(backgroundColor)
        }
        background = shape
    }
}