package com.example.thirdparty.test;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

/**
 * Created by zhuangsj on 17-8-12.
 */

public class TestViewPager extends ViewPager{

    public TestViewPager(Context context) {
        super(context);
    }

    public TestViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //L.d("TestViewPager  dispatchTouchEvent" , ev.getAction() + "  " + check());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //L.d("TestViewPager  onInterceptTouchEvent" , ev.getAction() + "  " + check());
        return false;//super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //L.d("TestViewPager  onTouchEvent" , ev.getAction() + "  " + check());
        return super.onTouchEvent(ev);
    }

    private boolean check(){
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mFakeDragging");
            field.setAccessible(true);
            return (boolean) field.get(this);
        } catch (Exception e) {
            L.d("TestViewPager  check" , e.getMessage() + "");
            e.printStackTrace();
        }
        return false;
    }
}
