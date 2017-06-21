package com.example.thirdparty.rxjava;

import android.content.Context;

import java.util.List;

/**
 * Created by zhuangsj on 16-11-2.
 */

public interface RxJavaContacts {

    interface IView {
        void updateList(List<Model> models);
    }

    interface IPresenter {
        void getData();
    }
}
