package com.example.thirdparty.rxjava;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by zhuangsj on 16-11-2.
 */

public class ApiHelper {
    private static final String TAG = "zhuangsj";

    static Observable<HttpResult> getPluginLists(final Context context, int v) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        File httpCacheDirectory = new File("/storage/emulated/0", "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        builder.cache(cache);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.d(TAG, "intercept() returned: " + request.url() + "," + isNetworkReachable(context));
                if (!isNetworkReachable(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .url(request.url()).build();
                }

                Response response = chain.proceed(request);
                if (isNetworkReachable(context)) {
                    int maxAge = 60 * 60; // read from cache for 1 minute
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
        builder.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://update/synth/open/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();


        PluginModle pluginModle = retrofit.create(PluginModle.class);
        return pluginModle.getPluginLists(v);
    }

    static Observable<HttpResult> getPluginLists2(final Context context, int v) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        File httpCacheDirectory = new File("/storage/emulated/0", "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        builder.cache(cache);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
//                String cacheControl = originalResponse.header("Cache-Control");
//                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
//                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
//                    return originalResponse.newBuilder()
//                            .header("Cache-Control", "public, max-age=" + 111)
//                            .build();
//                } else {
//                    return originalResponse;
//                }
                if (isNetworkReachable(context)) {
                    int maxAge = 20; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 20; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
        builder.addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR);

        Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkReachable(context)) {
                    request = request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached")
                            .build();
                }
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://update.gionee.com/synth/open/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();


        PluginModle pluginModle = retrofit.create(PluginModle.class);
        return pluginModle.getPluginLists(v);
    }

    static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }
}
