package com.droidcba.kedditbysteps.di.news

import com.droidcba.kedditbysteps.di.AppModule
import com.droidcba.kedditbysteps.di.NetworkModule
import com.example.thirdparty.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 *
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NewsModule::class,
        NetworkModule::class)
)
interface NewsComponent {

    fun inject(mainActivity: MainActivity)

}