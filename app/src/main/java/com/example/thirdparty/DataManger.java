package com.example.thirdparty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangsj on 17-6-22.
 */

public class DataManger {

    public static List<ImageInfo> getDatas(int size) {
        List<ImageInfo> mDatas = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageInfo info = new ImageInfo();
            info.setName("任务" + i);
            info.setState(Presenter.State.IDLE);
            info.setProgress(i * 12 % 100);
            mDatas.add(info);
        }
        return mDatas;
    }
}
