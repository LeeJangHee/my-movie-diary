package com.devlee.mymoviediary.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.devlee.mymoviediary.presentation.activity.ProgressControl

abstract class BaseFragment<V : ViewBinding> : Fragment() {
    private var _binding: V? = null
    protected val binding get() = _binding!!

    private var toolbarControl: ToolbarControl? = null
    private var progressControl: ProgressControl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fa = requireActivity()
        toolbarControl = fa as ToolbarControl
        progressControl = fa as ProgressControl
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

    open fun setSearchToolbar(
        editorActionListener: TextView.OnEditorActionListener? = null,
        removeClickListener: View.OnClickListener? = null
    ) {
        toolbarControl?.setToolbarSearch(editorActionListener, removeClickListener)
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

    open fun setMenuToolbar(type: Int, view: View) {
        toolbarControl?.setToolbarMenu(type, view)
    }

    open fun clearMenu() {
        toolbarControl?.clearMenu()
    }

    abstract fun getLayoutId(): Int

    abstract fun setView()

    open fun showProgressDialog() {
        progressControl?.showProgress()
    }

    open fun dismissProgressDialog() {
        progressControl?.dismissProgress()
    }

    interface ToolbarControl {
        fun setToolbarSearch(
            editorActionListener: TextView.OnEditorActionListener? = null,
            removeClickListener: View.OnClickListener? = null
        )

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

        fun setToolbarMenu(
            type: Int,
            view: View
        )

        fun clearMenu()
    }
}