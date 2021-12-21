package com.devlee.mymoviediary.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivityDefaultBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), BaseFragment.ToolbarControl {
    protected lateinit var binding: V
    private lateinit var defaultBinding: ActivityDefaultBinding

    // ActionBar
    private var appbarLayoutBinding: LayoutAppbarBinding? = null
    protected var appToolbarLayout: AppToolbarLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (getLayout() != 0) {
            defaultBinding = DataBindingUtil.setContentView(this, R.layout.activity_default)
            binding = DataBindingUtil.inflate(layoutInflater, getLayout(), defaultBinding.layoutContent.layoutBase, false)
            defaultBinding.layoutContent.layoutBase.addView(binding.root)
            appbarLayoutBinding = defaultBinding.layoutAppbar
        }

        appbarLayoutBinding?.let { appbarBinding ->
            appToolbarLayout = AppToolbarLayout(this, appbarBinding)

            setToolbar()
        }
    }

    open fun getLayout(): Int = 0

    abstract fun setToolbar()

    override fun setToolbarTitle(
        title: String,
        leftImage: Int?,
        rightImage: Int?,
        onClickListener: View.OnClickListener?
    ) {
        appToolbarLayout?.setTitleView(title, leftImage, rightImage, onClickListener)
    }

    override fun setToolbarMenu(type: Int, @DrawableRes resId: Int, onClickListener: View.OnClickListener?) {
        appToolbarLayout?.setImageMenu(type, resId, onClickListener)
    }
}