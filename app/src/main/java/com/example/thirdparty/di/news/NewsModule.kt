package com.droidcba.kedditbysteps.di.news

import com.droidcba.kedditbysteps.api.NewsAPI
import com.droidcba.kedditbysteps.api.NewsRestAPI
import com.droidcba.kedditbysteps.api.GankApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *
 */
@Module
class NewsModule {

    @Provides
    @Singleton
    fun provideNewsAPI(gankApi: GankApi): NewsAPI = NewsRestAPI(gankApi)

    @Provides
    @Singleton
    fun provideGankApi(retrofit: Retrofit): GankApi = retrofit.create(GankApi::class.java)
}
