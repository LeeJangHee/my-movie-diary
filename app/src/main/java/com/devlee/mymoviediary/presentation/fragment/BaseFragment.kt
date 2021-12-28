package com.devlee.mymoviediary.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewBinding> : Fragment() {
    private var _binding: V? = null
    protected val binding get() = _binding!!

    private var toolbarControl: ToolbarControl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fa = requireActivity()
        if (fa is ToolbarControl) {
            toolbarControl = fa
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (getLayoutId() > 0) {
            _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            binding.root
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    open fun setTitleToolbar(
        title: String,
        subTitle: String? = null,
        leftImage: Int? = null,
        rightImage: Int? = null,
        onClickListener: View.OnClickListener? = null
    ) {
        toolbarControl?.setToolbarTitle(title, subTitle, leftImage, rightImage, onClickListener)
    }

    open fun setMenuToolbar(
        type: Int,
        @DrawableRes resId: Int? = null,
        @StringRes strId: Int? = null,
        onClickListener: View.OnClickListener? = null
    ) {
        toolbarControl?.setToolbarMenu(type, resId, strId, onClickListener)
    }

    open fun getLayoutId(): Int = 0

    abstract fun setView()

    interface ToolbarControl {
        fun setToolbarTitle(
            title: String,
            subTitle: String? = null,
            leftImage: Int? = null,
            rightImage: Int? = null,
            onClickListener: View.OnClickListener? = null
        )

        fun setToolbarMenu(
            type: Int,
            @DrawableRes resId: Int? = null,
            @StringRes strId: Int? = null,
            onClickListener: View.OnClickListener? = null
        )
    }

}