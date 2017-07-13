package com.example.thirdparty

import android.app.Application
import com.droidcba.kedditbysteps.di.AppModule

/**
 *
 */
class GankApp : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
    }
}