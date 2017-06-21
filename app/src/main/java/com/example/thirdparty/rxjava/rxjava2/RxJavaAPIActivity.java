package com.example.thirdparty.rxjava.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.thirdparty.BaseLogActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
