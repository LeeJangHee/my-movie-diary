package com.devlee.mymoviediary.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
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
        leftImage: Int? = null,
        rightImage: Int? = null,
        leftClickListener: View.OnClickListener? = null,
        rightClickListener: View.OnClickListener? = null
    ) {
        toolbarControl?.let {
            it.setToolbarTitle(title, leftImage, rightImage, leftClickListener)
        }
    }

    open fun setMenuToolbar(
        type: Int,
        @DrawableRes resId: Int,
        onClickListener: View.OnClickListener? = null
    ) {
        toolbarControl?.let {
            it.setToolbarMenu(type, resId, onClickListener)
        }
    }

    open fun getLayoutId(): Int = 0

    abstract fun setView()

    interface ToolbarControl {
        fun setToolbarTitle(
            title: String,
            leftImage: Int? = null,
            rightImage: Int? = null,
            leftClickListener: View.OnClickListener? = null,
            rightClickListener: View.OnClickListener? = null
        )

        fun setToolbarMenu(
            type: Int,
            @DrawableRes resId: Int,
            onClickListener: View.OnClickListener? = null
        )
    }

}