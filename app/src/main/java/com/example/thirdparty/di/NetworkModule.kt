package com.droidcba.kedditbysteps.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOKHttp(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addNetworkInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY)})
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client:OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }
}