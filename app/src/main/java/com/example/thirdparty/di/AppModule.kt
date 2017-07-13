package com.droidcba.kedditbysteps.di

import android.app.Application
import android.content.Context
import com.example.thirdparty.GankApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 *
 */
@Module
class AppModule(val app: GankApp) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideApplication() : Application = app

}
