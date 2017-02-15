package com.example.thirdparty.rxjava.rxbus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.R;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class RxBusActivity extends BaseActivity {

    public static final String TAG = "zhuangsj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);
    }

    public void onStart(View v) {
        Observable<String> observable = Observable.just("Event")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e(TAG,"Expensive operation for " + s);
                        return s;
                    }
                })
                .publish()
//                .refCount();
                .autoConnect(2, new Action1<Subscription>() {
                    @Override
                    public void call(Subscription subscription) {
                        Log.e(TAG,"subscription got: " + subscription);
                    }
                });

//        observable.subscribe(s -> Log.e(TAG,"Sub1 got: " + s));
//        observable.subscribe(s -> Log.e(TAG,"Sub2 got: " + s));
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"Sub1 got: " + s);
            }
        });
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"Sub2 got: " + s);
            }
        });
    }

    public void onPause(View v) {

    }

}
