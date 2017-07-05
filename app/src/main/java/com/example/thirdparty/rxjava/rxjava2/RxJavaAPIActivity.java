package com.example.thirdparty.rxjava.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.thirdparty.BaseLogActivity;
import com.example.thirdparty.R;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxJavaAPIActivity extends BaseLogActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActionButton("simple", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());
            }
        });

        addActionButton("interval + map", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                Observable.interval(0, 2, TimeUnit.SECONDS).range(0, 10).map(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer s) throws Exception {
                        return "result="+s;
                    }
                }).subscribe(getObserver());
            }
        });

        addActionButton("Single", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                Single.just("A")
                        .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return "result="+s;
                    }
                }).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        updateProcess(" onSubscribe : " + disposable.isDisposed());
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        updateProcess(s);
                    }
                });
            }
        });


        // 按顺序发送数据
        addActionButton("Concat", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();

                final String[] aStrings = {"A1", "A2", "A3", "A4"};
                final String[] bStrings = {"B1", "B2", "B3"};

                final Observable<String> aObservable = Observable.fromArray(aStrings);
                final Observable<String> bObservable = Observable.fromArray(bStrings);

                Observable.concat(aObservable, bObservable)
                        .subscribe(getObserver());
            }
        });


        // Merge 不保证发送的顺序
        addActionButton("Merge", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();

                final String[] aStrings = {"A1", "A2", "A3", "A4", "A5", "A6", "A7"};
                final String[] bStrings = {"B1", "B2", "B3"};

                final Observable<String> aObservable = Observable.fromArray(aStrings);
                final Observable<String> bObservable = Observable.fromArray(bStrings);

                Observable.merge(aObservable, bObservable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getObserver());
            }
        });

        addActionButton("retryWhen", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                /**
                 * retryWhen 里的函数只创建一次，返回的Obserable会注册inner , 每次收到onError就会向这个Obserable发送onNext，
                 * 最后传递给inner，inner再决定要不要续定。
                 */
                Single someObservable = Observable
                        .fromIterable(Arrays.asList(new Integer[]{2, 3, 5, 7, 11, 8, 18}))
                        .doOnNext(result -> updateProcess(" doOnNext1 " + result))
                        .filter(prime -> prime % 2 == 0)
                        .doOnNext(result -> updateProcess(" doOnNext2 " + result))
                        .count();

                Observable mObservable = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        for (int i = 0; i < 10; i++) {
                            e.onNext("" + i);
                            if(i == 8)
                                e.onError(new Exception("not support"));
                        }
                        e.onComplete();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateProcess("doOnError ..." + throwable.getMessage());
                    }
                }).retryWhen(attempts -> {
                    updateProcess("retryWhen create ...");
                    return attempts.zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(new Function<Integer, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Integer integer) throws Exception {
                            updateProcess("delay retry by " + integer + " second(s)");
                            if(integer >= 3)
                                return Observable.error(new IllegalArgumentException("not support 3"));
                            return Observable.timer(integer, TimeUnit.SECONDS);
                        }
                    });
                });

                mObservable.subscribe(getObserver());//result -> updateProcess(" result " + result),error -> updateProcess(" error " + error));
            }
        });
    }

    private Observable<String> getObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i = 0; i < 10 && !e.isDisposed(); i++) {
                    e.onNext("" + i);
                }
                e.onComplete();
            }
        });
    }

    private Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                updateProcess(" onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(String userList) {
                updateProcess(userList);
            }

            @Override
            public void onError(Throwable e) {
                updateProcess(" onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                updateProcess(" onComplete");
            }
        };
    }
}
