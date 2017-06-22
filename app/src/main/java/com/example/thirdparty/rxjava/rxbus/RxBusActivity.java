package com.example.thirdparty.rxjava.rxbus;

import android.os.Bundle;
import android.view.View;

import com.example.thirdparty.BaseLogActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxBusActivity extends BaseLogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearProcess();

        ObservableBus.getInstance().toObservable(String.class).subscribe(getObserver());
        addActionButton(" Observable RxBus ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                ObservableBus.getInstance().post(123);
                ObservableBus.getInstance().post("Observable");
            }
        });


        /**
         * 对于Flowable，要发送数据之前要先调用 subscriber.request , 不然会出错
         *
         */
        FlowableBus.getInstance().toFlowable(String.class)
                .onBackpressureDrop(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        // 缓存不了的数据，会被放弃并回调这里
                        updateProcess("Consumer  : " + s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber());

        addActionButton(" Flowable RxBus ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearProcess();
                for (int i = 0; i < 200; i++) {
                    FlowableBus.getInstance().post(" i = " + i);
                }
            }
        });

    }

    private Observer<String> getObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                updateProcess("Observer onSubscribe " + d.isDisposed());
            }

            @Override
            public void onNext(String userList) {
                updateProcess(userList);
            }

            @Override
            public void onError(Throwable e) {
                updateProcess("Observer onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                updateProcess("Observer onComplete");
            }
        };
    }

    private Subscriber<String> getSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                updateProcess("Subscriber onSubscribe");
                s.request(Integer.MAX_VALUE);
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
