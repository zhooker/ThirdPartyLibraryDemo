package com.example.thirdparty.retrofit.download;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zhuangsj on 16-11-2.
 */

public interface PluginApkModle {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
}
