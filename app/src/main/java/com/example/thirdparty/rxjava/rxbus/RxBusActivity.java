package com.example.thirdparty.rxjava.rxbus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.BaseLogActivity;
import com.example.thirdparty.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxBusActivity extends BaseLogActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addActionButton("rxBus", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                testBus();
            }
        });

        addActionButton("test", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                start();
            }
        });

        addActionButton("loadData", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                loadData();
            }
        });
    }

    public void testBus() {
        RxBus.getInstance().toObserverable(String.class)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String studentEvent) {
                        updateProcess("String result " + studentEvent);
                    }
                });

        RxBus.getInstance().toObserverable(Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer studentEvent) {
                        updateProcess("Integer result " + studentEvent);
                    }
                });

        RxBus.getInstance().post("Hello");
        RxBus.getInstance().post(999);
    }

    public void start() {
        Observable<String> observable = Observable.just("Event")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        updateProcess("Expensive operation for " + s);
                        return s;
                    }
                })
                .publish()
//                .refCount();
                .autoConnect(2, new Action1<Subscription>() {
                    @Override
                    public void call(Subscription subscription) {
                        updateProcess("subscription got: " + subscription);
                    }
                });

//        observable.subscribe(s -> Log.e(TAG,"Sub1 got: " + s));
//        observable.subscribe(s -> Log.e(TAG,"Sub2 got: " + s));
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                updateProcess("Sub1 got: " + s);
            }
        });
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                updateProcess("Sub2 got: " + s);
            }
        });
    }

    public void loadData() {
        // 测试连续下载５个目标，如果其中一个目标失败了就重试３次
        Observable.just(1, 2, 3, 4, 5).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(final Integer integer) {
                updateProcess("flatMap begin got: " + integer);
                return Observable.create(new Observable.OnSubscribe<String>() {


                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            updateProcess("id=" + integer + ",wait " + i);
                            if (i == 3 && integer == 2)
                                subscriber.onError(new IllegalArgumentException("IllegalArgumentException " + integer));
                        }
                        subscriber.onNext("result=" + integer);
                        subscriber.onCompleted();
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Object>() {
                            @Override
                            public Object call(Throwable throwable, Integer integer) {
                                return integer;
                            }
                        }).delay(1, TimeUnit.SECONDS);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {

            @Override
            public void onCompleted() {
                updateProcess("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                updateProcess("Error : " + e.getMessage());
            }

            @Override
            public void onNext(String responseBody) {
                updateProcess("onNext : " + responseBody);
            }
        });
    }
}
