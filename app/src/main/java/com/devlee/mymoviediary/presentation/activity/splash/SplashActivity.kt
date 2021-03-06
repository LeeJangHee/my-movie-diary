package com.devlee.mymoviediary.presentation.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivitySplashBinding
import com.devlee.mymoviediary.presentation.activity.BaseActivity
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.utils.gone

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavLayout?.gone()
        Handler().postDelayed(delayInMillis = 1000L) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun getLayout(): Int = R.layout.activity_splash
    override fun setToolbar() {
        appToolbarLayout?.gone()
    }
}