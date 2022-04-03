package com.devlee.mymoviediary

import android.app.Application
import com.devlee.mymoviediary.utils.SharedPreferencesUtil


class App : Application() {

    companion object {
        @Volatile
        private var instance: App? = null

        @JvmStatic
        fun getInstance(): App =
            instance ?: synchronized(this) {
                instance ?: App().also {
                    instance = it
                }
            }
    }

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.init(this)
        instance = this
    }
}