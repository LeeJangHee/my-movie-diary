package com.devlee.mymoviediary.presentation.activity.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivityMainBinding
import com.devlee.mymoviediary.presentation.activity.BaseActivity
import com.devlee.mymoviediary.utils.show

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavLayout?.show()

        val navFragment = supportFragmentManager.findFragmentById(R.id.mainNavContainerView) as NavHostFragment
        navController = navFragment.navController
        bottomNavLayout?.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainHomeFragment, R.id.mainCalenderFragment, R.id.mainCategoryFragment, R.id.mainProfileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun getLayout(): Int = R.layout.activity_main

}