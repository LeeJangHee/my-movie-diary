package com.devlee.mymoviediary.presentation.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.toDp
import com.google.android.material.shape.*

abstract class BaseDialogFragment<V : ViewBinding>(@LayoutRes private val layoutId: Int) : DialogFragment() {

    companion object {
        private const val CORNER_FAMILY_ROUND = CornerFamily.ROUNDED
    }
    private var _binding: V? = null
    protected val binding get() = _binding!!

    private val shapeAppearanceModel get() = ShapeAppearanceModel().toBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return if (_binding != null) {
            binding.root
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setView()
    }

    fun setTopCornerRadius(left: Int, right: Int) {
        val model = shapeAppearanceModel.apply {
            setTopRightCorner(CORNER_FAMILY_ROUND, right.toDp())
            setTopLeftCorner(CORNER_FAMILY_ROUND, left.toDp())
        }.build()
        setCornerRadius(model)
    }

    fun setBottomCornerRadius(left: Int, right: Int) {
        val model = shapeAppearanceModel.apply {
            setBottomLeftCorner(CORNER_FAMILY_ROUND, left.toDp())
            setBottomRightCorner(CORNER_FAMILY_ROUND, right.toDp())
        }.build()

        setCornerRadius(model)
    }

    fun setCornerRadius(size: Float) {
        val model = shapeAppearanceModel.apply {
            setAllCorners(CORNER_FAMILY_ROUND, size)
        }.build()
        setCornerRadius(model)
    }

    private fun setCornerRadius(model: ShapeAppearanceModel) {
        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = getColorRes(requireContext(), R.color.white)
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        binding.root.background = shape
    }

    abstract fun setView()
}