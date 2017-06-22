package com.example.thirdparty.rxjava.flowable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.thirdparty.BaseLogActivity;
import com.example.thirdparty.rxjava.rxbus.ObservableBus;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class FlowableActivity extends BaseLogActivity {


    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addActionButton(" Begin Flowable ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                if(mSubscription != null)
                    mSubscription.cancel();

                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < Integer.MAX_VALUE && !e.isCancelled(); i++) {
                            e.onNext(i);
                            if(i % 1000 == 0)
                                updateProcess("----------------- " + i);
                            try{
                                Thread.sleep(1);
                            } catch (Exception w) {

                            }
                        }
                    }
                }, BackpressureStrategy.DROP)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Integer, String>() {
                            @Override
                            public String apply(@NonNull Integer integer) throws Exception {
                                return "" + integer;
                            }
                        })
                        .subscribe(getSubscriber());
            }
        });

        addActionButton(" Request Data ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSubscription != null)
                    mSubscription.request(128);
                else
                    updateProcess("-mSubscription is null ");
            }
        });
    }


    private Subscriber<String> getSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
                updateProcess("Subscriber onSubscribe");
            }

            @Override
            public void onNext(String s) {
                updateProcess(s);
            }

            @Override
            public void onError(Throwable t) {
                updateProcess("Subscriber onError : " + t.getMessage());
            }

            @Override
            public void onComplete() {
                updateProcess("Subscriber onComplete");
            }
        };
    }
}
