package com.example.thirdparty.rxjava.rxbus;

/**
 * Created by zhuangsj on 17-2-16.
 * 参考 : https://lingyunzhu.github.io/2016/03/01/RxBus%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%8F%8A%E7%AE%80%E5%8D%95%E4%BD%BF%E7%94%A8/
 */
public class RxBus {
    private static volatile RxBus mDefaultInstance;

    private RxBus() {

    }

    public static RxBus getInstance() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

}