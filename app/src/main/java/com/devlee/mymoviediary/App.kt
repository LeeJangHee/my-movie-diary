package com.devlee.mymoviediary

import android.app.Application
import com.devlee.mymoviediary.utils.SharedPreferencesUtil


class App : Application() {

    companion object {
        private class AppHelper {
            companion object {
                val INSTANCE = App()
            }
        }

        fun getInstance() = AppHelper.INSTANCE
    }

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.init(this)
    }
}