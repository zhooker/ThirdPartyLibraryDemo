package com.example.thirdparty.rxjava;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhuangsj on 16-11-2.
 */

public class DataPresenter implements RxJavaContacts.IPresenter {

    private static final String TAG = "zhuangsj";

    private Context context;
    private RxJavaContacts.IView mView;
    private int currVersion = 0;

    public DataPresenter(RxJavaContacts.IView mView) {
        this.mView = mView;
        this.context = (Context) mView;

        currVersion = PreferenceManager.getDefaultSharedPreferences(context).getInt("Version", 0);
    }

    @Override
    public void getData() {
        Log.d(TAG, "getData() currVersion: " + currVersion);
        ApiHelper.getPluginLists2(context, currVersion).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpResult, List<Model>>() {
                    @Override
                    public List<Model> call(HttpResult httpResult) {
                        return httpResult.ps;
                    }
                }).subscribe(new Subscriber<List<Model>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() returned: " + e.getMessage());
                mView.updateList(null);
            }

            @Override
            public void onNext(List<Model> user) {
                Log.d(TAG, "onNext() returned " + (user == null ? 0 : user.size()));
                mView.updateList(user);
            }

        });
    }


}
