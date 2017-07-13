package com.droidcba.kedditbysteps.api

import com.droidcba.kedditbysteps.commons.GankNews
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GankApi {
    @GET("/api/data/{category}/{count}/{page}")
    fun getNews(@Path("category") category: String,
               @Path("page") page: Int, @Path("count") count: Int): Observable<GankNews>
}