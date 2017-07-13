package com.droidcba.kedditbysteps.api

import com.droidcba.kedditbysteps.commons.GankNews
import io.reactivex.Observable

class NewsRestAPI constructor(private val gankApi: GankApi) : NewsAPI {

    override fun getNews(category: String, page: Int, count: Int): Observable<GankNews> {
        return gankApi.getNews(category, page, count)
    }
}