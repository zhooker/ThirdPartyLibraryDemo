package com.example.thirdparty.rxjava.rxbus;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by zhuangsj on 17-6-16.
 */
public class FlowableBus {

    private static FlowableBus mInstance;
    private FlowableProcessor mSubject;

    private FlowableBus(FlowableProcessor subject) {
        mSubject = subject;
    }

    public static FlowableBus getInstance() {
        if(mInstance == null)
        {
            synchronized (FlowableBus.class) {
                if(mInstance == null)
                    mInstance = new FlowableBus(PublishProcessor.create());
            }
        }
        return mInstance;
    }

    private FlowableProcessor get() {
        return mSubject;
    }


    public <T> Flowable<T> toFlowable(Class<T> clazz) {
        return get().ofType(clazz);
    }

    public Flowable toFlowable() {
        return get();
    }

    public void post(Object obj) {
        get().onNext(obj);
    }

    public boolean hasSubscribers() {
        return get().hasSubscribers();
    }
}