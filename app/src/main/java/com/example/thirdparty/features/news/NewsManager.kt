package com.droidcba.kedditbysteps.features.news

import com.droidcba.kedditbysteps.api.NewsAPI
import com.droidcba.kedditbysteps.commons.GankNews
import io.reactivex.Observable
import javax.inject.Inject

/**
 * News Manager allows you to request news from Gank API.
 *
 */
class NewsManager @Inject constructor(private val api: NewsAPI) {

    /**
     *
     * Returns Gank News paginated by the given limit.
     *
     */
    fun getNews(category: String, page: Int, count: Int = 10): Observable<GankNews> {
        return api.getNews(category,page, count)
    }
}