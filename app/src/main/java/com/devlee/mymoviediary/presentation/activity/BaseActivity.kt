package com.devlee.mymoviediary.presentation.activity

import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
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
import com.devlee.mymoviediary.utils.isMainBottomNavLayout
import com.devlee.mymoviediary.utils.loadingLiveData
import com.devlee.mymoviediary.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), BaseFragment.ToolbarControl, ProgressControl {
    private companion object {
        private const val TAG = "BaseActivity"
    }
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

        hideBottomNavTooltip()

        var imeShown = false
        // 키보드가 올라올 경우 bottom nav layout 숨김
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            fun checkIMEShow(): Boolean {
                val rootViewHeight: Int = binding.root.rootView.height
                val r: Rect = Rect()
                binding.root.getWindowVisibleDisplayFrame(r)
                val keyboardHeight: Int = rootViewHeight - r.bottom

                return (keyboardHeight > rootViewHeight * 0.15)
            }

            checkIMEShow().also {
                if (isMainBottomNavLayout.value == false) return@addOnGlobalLayoutListener
                if (imeShown != it) {
                    Log.d(TAG, "IME show? = $it")
                    imeShown = it

                    bottomNavLayout?.show(!imeShown)
                }
            }
        }

        loadingLiveData.observe(this) {
            if (it) showProgressDialog()
            else dismissProgressDialog()
        }
    }

    /** bottom nav layout long click -> 이름 보이지 않기 */
    private fun hideBottomNavTooltip() {
        bottomNavLayout?.menu?.forEach {
            val view = this.findViewById<View>(it.itemId)
            view.setOnLongClickListener { true }
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

    open fun setToolbar() {}

    override fun setToolbarSearch(editorActionListener: TextView.OnEditorActionListener?, removeClickListener: View.OnClickListener?) {
        appToolbarLayout?.setSearchView(editorActionListener, removeClickListener)
    }

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