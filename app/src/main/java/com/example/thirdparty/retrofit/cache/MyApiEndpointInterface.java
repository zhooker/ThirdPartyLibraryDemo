package com.example.thirdparty.retrofit.cache;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by zhuangsj on 17-7-3.
 */

public interface MyApiEndpointInterface {

    @GET("api/data/{catagory}/10/1")
    Call<Entry> getEntry(@Path("catagory") String catagory, @Header("Cache-Control") RetrofitCacheActivity.CACHE cache);
}
