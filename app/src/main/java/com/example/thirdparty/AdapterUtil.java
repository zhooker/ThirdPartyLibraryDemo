package com.example.thirdparty;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.Button;

/**
 * Created by zhuangsj on 16-9-24.
 * 这里是公共函数，主要用来设置Buton在不同状态时的提示语。
 * 使用BindingAdapter时，函数一定要是static,第一个参数是对应的View类
 */

public class AdapterUtil {

    @BindingAdapter("update")
    public static void updateText(Button btn, Presenter.State state) {

        switch (state) {
            case DOWNLOADING:
                btn.setText("下载中");
                break;
            case PAUSE:
                btn.setText("暂停");
                break;
            case FINISH:
                btn.setText("完成");
                break;
            default:
                btn.setText("下载");
        }
    }
}
