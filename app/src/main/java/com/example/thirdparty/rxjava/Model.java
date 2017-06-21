package com.example.thirdparty.rxjava;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangsj on 16-11-2.
 */

public class Model {

    String name;

    @SerializedName("icon")
    String url_icon;

    @SerializedName("rn")
    String description;


    @Override
    public String toString() {
        return "Model{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", url_icon='" + url_icon + '\'' +
                '}';
    }
}
