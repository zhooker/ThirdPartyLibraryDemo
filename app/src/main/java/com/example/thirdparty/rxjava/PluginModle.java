package com.example.thirdparty.rxjava;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhuangsj on 16-11-2.
 */

public interface PluginModle {
//    @FormUrlEncoded
//    @POST("checkplugin.do")
//    Observable<HttpResult> getPluginLists(@Field("v") int v
//            , @Field("rom") String rom, @Field("p") String p, @Field("model") String model, @Field("pv") String pv);

//    @FormUrlEncoded
//    @POST("checkplugin.do")
//    Observable<HttpResult> getPluginLists(@FieldMap Map<String, String> params);

    //QueryMap
    @GET("checkplugin.do?rom=WBL7517A01_A_T0218&p=com.android.camera&model=GN8002&pv=40100018")
    Observable<HttpResult> getPluginLists(@Query("v") int v);
}
