package com.devlee.mymoviediary.presentation.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivityDefaultBinding
import com.devlee.mymoviediary.databinding.LayoutAppbarBinding
import com.devlee.mymoviediary.databinding.LayoutBottomNavBaseBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.presentation.layout.ProgressDialog
import com.devlee.mymoviediary.utils.gone
import com.devlee.mymoviediary.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), BaseFragment.ToolbarControl, ProgressControl {
    protected lateinit var binding: V
    private lateinit var defaultBinding: ActivityDefaultBinding

    // ActionBar
    private var appbarLayoutBinding: LayoutAppbarBinding? = null
    protected var appToolbarLayout: AppToolbarLayout? = null

    // Bottom Nav
    private var bottomNavLayoutBinding: LayoutBottomNavBaseBinding? = null
    protected var bottomNavLayout: BottomNavigationView? = null

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (getLayout() != 0) {
            defaultBinding = DataBindingUtil.setContentView(this, R.layout.activity_default)
            binding = DataBindingUtil.inflate(layoutInflater, getLayout(), defaultBinding.layoutContent.layoutBase, false)
            defaultBinding.layoutContent.layoutBase.addView(binding.root)
            appbarLayoutBinding = defaultBinding.layoutAppbar
            bottomNavLayoutBinding = defaultBinding.layoutBottomNav
        }

        appbarLayoutBinding?.let { appbarBinding ->
            appToolbarLayout = AppToolbarLayout(this, appbarBinding)

            setToolbar()
        }

        bottomNavLayoutBinding?.let { bottomBinding ->
            bottomNavLayout = bottomBinding.baseBottomNav
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }

    abstract fun getLayout(): Int

    open fun hideBottomNav() {
        bottomNavLayoutBinding?.root?.gone()
    }

    open fun showBottomNav() {
        bottomNavLayoutBinding?.root?.show()
    }

    open fun getAppbar() = appToolbarLayout

    abstract fun setToolbar()

    override fun setToolbarTitle(
        title: String,
        subTitle: String?,
        leftImage: Int?,
        rightImage: Int?,
        onClickListener: View.OnClickListener?
    ) {
        appToolbarLayout?.setTitleView(title, subTitle, leftImage, rightImage, onClickListener)
    }

    override fun setToolbarMenu(type: Int, @DrawableRes resId: Int?, @StringRes strId: Int?, onClickListener: View.OnClickListener?) {
        appToolbarLayout?.setImageOrTextMenu(type, resId, strId, onClickListener)
    }

    override fun setToolbarMenu(type: Int, view: View) {
        appToolbarLayout?.setCustomView(view, type)
    }

    override fun clearMenu() {
        appToolbarLayout?.clearView()
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun dismissProgress() {
        dismissProgressDialog()
    }

    open fun showProgressDialog() {
        if (isFinishing) return

        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.showProgress()
    }

    open fun dismissProgressDialog() {
        progressDialog.dismissProgress()
    }
}

interface ProgressControl {
    fun showProgress()
    fun dismissProgress()
}