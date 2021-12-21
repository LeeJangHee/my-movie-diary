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

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navFragment = supportFragmentManager.findFragmentById(R.id.mainNavContainerView) as NavHostFragment
        navController = navFragment.navController
        binding.mainBottomNav.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainHomeFragment, R.id.mainCategoryFragment, R.id.mainProfileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavTooltip()
    }

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