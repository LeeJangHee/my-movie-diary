package com.devlee.mymoviediary.presentation.activity.main

import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivityMainBinding
import com.devlee.mymoviediary.presentation.activity.BaseActivity
import com.devlee.mymoviediary.utils.isMainBottomNavLayout
import com.devlee.mymoviediary.utils.show

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var imeShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navFragment = supportFragmentManager.findFragmentById(R.id.mainNavContainerView) as NavHostFragment
        navController = navFragment.navController
        binding.mainBottomNav.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainHomeFragment, R.id.mainCalenderFragment, R.id.mainCategoryFragment, R.id.mainProfileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        hideBottomNavTooltip()

        // 키보드가 올라올 경우 bottom nav layout 숨김
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            fun checkIMEShow(): Boolean {
                val rootViewHeight = binding.root.rootView.height
                val relativeHeight = binding.root.height
                return (rootViewHeight - relativeHeight > 200 * resources.displayMetrics.density)
            }

            checkIMEShow().also {
                if (isMainBottomNavLayout.value == false) return@addOnGlobalLayoutListener
                if (imeShown != it) {
                    imeShown = it

                    binding.mainBottomNav.show(!imeShown)
                }
            }
        }
    }

    /** bottom nav layout longclick -> 이름 보이지 않기 */
    private fun hideBottomNavTooltip() {
        binding.mainBottomNav.menu.forEach {
            val view = this.findViewById<View>(it.itemId)
            view.setOnLongClickListener { true }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun getLayout(): Int = R.layout.activity_main

    override fun setToolbar() {

    }
}