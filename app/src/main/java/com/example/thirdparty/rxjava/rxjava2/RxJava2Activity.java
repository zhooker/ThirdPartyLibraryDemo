package com.example.thirdparty.rxjava.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJava2Activity extends BaseActivity {
    public static final String TAG = "zhuangsj";
    private TextView mProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java2);
        mProcess = (TextView) findViewById(R.id.process);
    }

    private void updateProcess(String process) {
        String origin = mProcess.getText().toString();
        mProcess.setText(process + "\n" + origin);
    }

    private void clearProcess() {
        mProcess.setText("");
    }

    public void onStart(View v) {
        testDeBounce();
    }

    private void testConcat() {
        // 按顺序执行，如果用first()只会单纯等待到第一个执行完，如果用了first(action1)　会对第一个选择性判断．
        final Observable<String> memory = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
        Observable<String> disk = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                subscriber.onNext("disk");
                subscriber.onCompleted();
            }
        });

        Observable<String> network = Observable.just("network");


        Observable.concat(memory, disk, network)
//                .first()
                .first(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null;
                    }
                })
                .subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
        });
    }

    private void testAmb() {
        // 优先原则，谁先返回就一直用它的，另一个会被抛弃．
        final Observable<String> memory = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }
                    subscriber.onNext("memory : " + i);
                }
                subscriber.onCompleted();
            }
        });
        Observable<String> disk = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {

                    }
                    subscriber.onNext("disk : " + i);
                }
                subscriber.onCompleted();
            }
        });

        Observable.amb(memory, disk)
                .subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
        });
    }

    private void testMerge() {
        // 不保存输入的顺序
        Observable.merge(
                Observable.interval(250, TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "First";
                    }
                }),
                Observable.interval(150, TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "Second";
                    }
                }))
                .take(10)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext: " + s);
                    }
                });

    }

    private void testRetry() {
        // 对于retryWhen,收到onError直到onComplete才会触发．
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    Log.d(TAG, "create: " + i);
                    if (i < 5)
                        subscriber.onNext("data " + i);
                    else
                        subscriber.onError(new IllegalArgumentException("error " + i));
                }
                Log.d(TAG, "create end");
                subscriber.onCompleted();
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable;
            }
        })
//                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
//            @Override
//            public Observable<?> call(Observable<? extends Void> observable) {
//                Log.d(TAG, "repeatWhen... "+observable);
//                return observable.zipWith(Observable.range(1, 3), (n, i) -> i).delay(2, TimeUnit.SECONDS);
//            }
//        })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext: " + s);
                    }
                });
    }

    private Subscription mSubscription;

    private void testDeBounce() {
        //debounce 是时间内没有新的数据就发送，和sample不一样
//        if (mSubscription != null) {
//            mSubscription.unsubscribe();
//        }
        mSubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {

                    }
                    Log.d(TAG, "create: " + i);
                    subscriber.onNext("data " + i);
                }
                Log.d(TAG, "create end " + subscriber.isUnsubscribed() + "," + Thread.currentThread().getName());
                subscriber.onCompleted();
            }
        }).debounce(600, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }
        });
    }

    public void onPause(View v) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

}
