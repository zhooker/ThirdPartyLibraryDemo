package com.example.thirdparty.rxjava.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by zhuangsj on 17-6-16.
 */
public class ObservableBus {

    private static ObservableBus mInstance;
    private Subject mSubject;

    private ObservableBus(Subject subject) {
        mSubject = subject;
    }

    public static ObservableBus getInstance() {
        if(mInstance == null)
        {
            synchronized (ObservableBus.class) {
                if(mInstance == null)
                    mInstance = new ObservableBus(PublishSubject.create());
            }
        }
        return mInstance;
    }

    private Subject get() {
        return mSubject;
    }


    public <T> Observable<T> toObservable(Class<T> clazz) {
        return get().ofType(clazz);
    }

    public Observable toObservable() {
        return get();
    }

    public void post(Object obj) {
        get().onNext(obj);
    }

    public boolean hasObservers() {
        return get().hasObservers();
    }


}