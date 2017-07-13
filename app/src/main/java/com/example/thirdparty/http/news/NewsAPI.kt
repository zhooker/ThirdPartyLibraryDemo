package com.droidcba.kedditbysteps.api

import com.droidcba.kedditbysteps.commons.GankNews
import io.reactivex.Observable

/**
 * News API
 *
 */
interface NewsAPI {
    fun getNews(category: String, page: Int, count: Int): Observable<GankNews>
}