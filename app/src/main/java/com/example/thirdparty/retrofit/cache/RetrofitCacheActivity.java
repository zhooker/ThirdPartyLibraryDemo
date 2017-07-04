package com.example.thirdparty.retrofit.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.thirdparty.BaseLogActivity;
import com.example.thirdparty.retrofit.download.PluginApkModle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 使用Retrofit + 缓存 的例子
 */
public class RetrofitCacheActivity extends BaseLogActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File httpCacheDirectory = new File(getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        builder.addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR);
        //builder.addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        final MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        addActionButton("force request", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                Call<Entry> call = apiService.getEntry("Android",CACHE.NOCACHE);
                call.enqueue(new Callback<Entry>() {
                    @Override
                    public void onResponse(Call<Entry> call, retrofit2.Response<Entry> response) {
                        Entry entry = response.body();
                        updateProcess("onResponse : " + entry + "," + (entry==null?null:entry.getResults().size()));
                    }

                    @Override
                    public void onFailure(Call<Entry> call, Throwable t) {
                        updateProcess("onFailure  : " + t.getMessage());
                    }
                });
            }
        });

        addActionButton("only-if-cache request", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                Call<Entry> call = apiService.getEntry("Android",CACHE.CACHE);
                call.enqueue(new Callback<Entry>() {
                    @Override
                    public void onResponse(Call<Entry> call, retrofit2.Response<Entry> response) {
                        Entry entry = response.body();
                        updateProcess("onResponse : " + entry + "," + (entry==null?null:entry.getResults().size()));
                    }

                    @Override
                    public void onFailure(Call<Entry> call, Throwable t) {
                        updateProcess("onFailure  : " + t.getMessage());
                    }
                });
            }
        });
    }


    Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            if (isNetworkReachable(RetrofitCacheActivity.this)) {
                int maxAge = 50000; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkReachable(RetrofitCacheActivity.this)) {
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached")
                        .build();
            }
            return chain.proceed(request);
        }
    };

    static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }


    public enum CACHE {
        NOCACHE {
            @Override
            public String toString() {
                return "no-cache";
            }
        } ,
        CACHE {
            @Override
            public String toString() {
                return "public, only-if-cached";
            }
        }
    }
}
