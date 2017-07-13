package com.example.thirdparty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.droidcba.kedditbysteps.commons.GankNews
import com.droidcba.kedditbysteps.commons.InfiniteScrollListener
import com.droidcba.kedditbysteps.di.news.DaggerNewsComponent
import com.droidcba.kedditbysteps.features.news.NewsManager
import com.droidcba.kedditbysteps.features.news.adapter.NewsAdapter
import com.droidcba.kedditbysteps.features.news.adapter.NewsDelegateAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() , NewsDelegateAdapter.onViewSelectedListener {

    override fun onItemSelected(url: String?) {
        Toast.makeText(this, "onItemSelected  $url", Toast.LENGTH_LONG).show()
    }

    companion object {
        private val KEY_GANK_NEWS_LIST = "GankNews-list"
        private val KEY_GANK_NEWS_PAGE = "GankNews-page"
    }

    protected var subscriptions = CompositeDisposable()
    protected var page = 1

    @Inject lateinit var mNewsManager : NewsManager
    private var redditNews: GankNews? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerNewsComponent.builder()
                .appModule(GankApp.appModule)
                //.newsModule(NewsModule())
                .build().inject(this)

        news_list.apply {
                    setHasFixedSize(true)
                    val linearLayout = LinearLayoutManager(context)
                    layoutManager = linearLayout
                    clearOnScrollListeners()
                    addOnScrollListener(InfiniteScrollListener({ requestNews() }, linearLayout))
                    adapter = NewsAdapter(this@MainActivity)
         }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_GANK_NEWS_LIST)) {
            page = savedInstanceState.getInt(KEY_GANK_NEWS_PAGE)
            redditNews = savedInstanceState.get(KEY_GANK_NEWS_LIST) as GankNews
            (news_list.adapter as NewsAdapter).clearAndAddNews(redditNews!!.results)
        } else {
            requestNews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val news = (news_list.adapter as NewsAdapter).getNews()
        if (redditNews != null && news.isNotEmpty()) {
            outState.putParcelable(KEY_GANK_NEWS_LIST, redditNews?.copy(results = news))
            outState.putInt(KEY_GANK_NEWS_PAGE, page)
        }
    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
    }

    fun requestNews() {
        val subscription = mNewsManager.getNews("Android", page++)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { retrievedNews ->
                            redditNews = retrievedNews
                            (news_list.adapter as NewsAdapter).addNews(retrievedNews.results)
                        },
                        { e ->
                            Toast.makeText(this, e.message ?: "", Toast.LENGTH_LONG).show()
                        }
                )
        subscriptions.add(subscription)
    }
}
