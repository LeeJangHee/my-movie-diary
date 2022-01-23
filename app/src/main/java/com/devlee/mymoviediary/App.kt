package com.devlee.mymoviediary

import android.app.Application
import com.devlee.mymoviediary.utils.SharedPreferencesUtil

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.init(this)
    }
}