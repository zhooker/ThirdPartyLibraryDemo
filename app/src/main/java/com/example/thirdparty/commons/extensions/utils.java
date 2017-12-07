package com.example.thirdparty.commons.extensions;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

/**
 * Created by zhuangsj on 17-8-4.
 */

public class utils {
    public static <T extends RecyclerView> T findView(Activity activity, int id) {

         return (T) activity.findViewById(id);
      }

    public int[] removeDupliscat(int[] source) {


        int firstPos = 0, first = source[0];

        for (int i = 1; i < source.length; i++) {
            if(source[i] != first) {
                if(i - firstPos > 1) {
                    source[++firstPos] = source[i];
                } else {
                    firstPos = i;
                }
            }
        }

        return source;

    }
}
