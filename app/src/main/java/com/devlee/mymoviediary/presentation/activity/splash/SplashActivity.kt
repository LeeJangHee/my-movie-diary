package com.devlee.mymoviediary.presentation.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivitySplashBinding
import com.devlee.mymoviediary.presentation.activity.BaseActivity
import com.devlee.mymoviediary.presentation.activity.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(delayInMillis = 3000L) {
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