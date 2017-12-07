package com.example.thirdparty.test;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhuangsj on 17-8-12.
 */

public class TextView extends View {
    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.d("TextView dispatchTouchEvent" , ev.getAction() + "");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        L.d("TextView  onTouchEvent" , ev.getAction() + "");
        if(ev.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        else
            return false;

        //return super.onTouchEvent(ev);
    }
}
