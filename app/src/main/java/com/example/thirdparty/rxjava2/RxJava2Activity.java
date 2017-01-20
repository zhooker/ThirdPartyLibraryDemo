package com.example.thirdparty.rxjava2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.thirdparty.BaseActivity;
import com.example.thirdparty.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
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
        testMerge();
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
                        return s!=null;
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
                Observable.interval(250, TimeUnit.MILLISECONDS).map(i -> "First"),
                Observable.interval(150, TimeUnit.MILLISECONDS).map(i -> "Second"))
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

}
