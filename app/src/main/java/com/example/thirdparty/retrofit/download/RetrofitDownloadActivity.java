package com.example.thirdparty.retrofit.download;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 原文［Retrofit 2 - 如何从服务器下载文件］（http://www.jianshu.com/p/92bb85fc07e8#）
 */
public class RetrofitDownloadActivity extends BaseActivity {

    public static final String TAG = "zhuangsj";

    private TextView mProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_download);
        mProcess = (TextView) findViewById(R.id.process);
    }

    public void onStart(View v) {
        clearProcess();
        download();
    }


    private void updateProcess(String process) {
        String origin = mProcess.getText().toString();
        mProcess.setText(process + "\n" + origin);
    }

    private void clearProcess() {
        mProcess.setText("");
    }

    public Observable<ResponseBody> createDownloadService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://dl.gionee.com")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        PluginApkModle pluginModle = retrofit.create(PluginApkModle.class);
        return pluginModle.downloadFileWithDynamicUrlAsync("http://dl.gionee.com/synth/res/plugin/3/SmartBurst_V1.1.0.j.apk");
    }


    public void download() {
        Observable<ResponseBody> call = createDownloadService();
        call.flatMap(new Func1<ResponseBody, Observable<String>>() {
            @Override
            public Observable<String> call(ResponseBody responseBody) {
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        // todo change the file location/name according to your needs
                        File futureStudioIconFile = new File("/storage/emulated/0/test.apk");

                        InputStream inputStream = null;
                        OutputStream outputStream = null;

                        try {
                            byte[] fileReader = new byte[4096];

                            long fileSize = responseBody.contentLength();
                            long fileSizeDownloaded = 0;

                            inputStream = responseBody.byteStream();
                            outputStream = new FileOutputStream(futureStudioIconFile);

                            while (true) {
                                int read = inputStream.read(fileReader);

                                if (read == -1) {
                                    break;
                                }

                                outputStream.write(fileReader, 0, read);

                                fileSizeDownloaded += read;

                                float progress = (fileSizeDownloaded * 100.0f / fileSize);
                                Log.d(TAG, "file download  progress=" + progress);
                                subscriber.onNext(String.format("%.2f %%",progress));
                            }

                            outputStream.flush();

                            subscriber.onCompleted();
                        } catch (IOException e) {
                            Log.d(TAG, "file download error: " + e);
                            subscriber.onError(e);
                        } finally {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }

                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        })
                .sample(100, TimeUnit.MILLISECONDS)
//                .onBackpressureDrop()
//                .onBackpressureBuffer(1000)
                .doOnRequest(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d(TAG, "doOnRequest : " + aLong);
                updateProcess("doOnRequest : " + aLong);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onStart() {
//                request(1);
            }
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
                updateProcess("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError e=" + e);
                updateProcess("Error : " + e.getMessage());
            }

            @Override
            public void onNext(String responseBody) {
                updateProcess(responseBody);
                Log.d(TAG, "responseBody=" + responseBody);
//                request(100);
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File("/storage/emulated/0/test.apk");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                Log.d(TAG, "file download error: " + e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
