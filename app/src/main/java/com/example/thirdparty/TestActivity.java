package com.example.thirdparty;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.thirdparty.test.L;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        List<View> advPics = new ArrayList<View>();
        ImageView img1 = new ImageView(this);
        img1.setBackgroundResource(R.mipmap.ic_launcher);
        img1.setBackgroundColor(Color.RED);
        advPics.add(img1);
        ImageView img2 = new ImageView(this);
        img2.setBackgroundResource(R.mipmap.ic_launcher);
        img2.setBackgroundColor(Color.YELLOW);
        advPics.add(img2);
        ImageView img3 = new ImageView(this);
        img3.setBackgroundResource(R.mipmap.ic_launcher);
        img3.setBackgroundColor(Color.DKGRAY);
        advPics.add(img3);
        ImageView img4 = new ImageView(this);
        img4.setBackgroundResource(R.mipmap.ic_launcher);
        img4.setBackgroundColor(Color.GRAY);
        advPics.add(img4);

        ViewPager advPager = (ViewPager) findViewById(R.id.adv_pager);
        advPager.setAdapter(new AdvAdapter(advPics));
//        advPager.setPageMargin(-50);
        advPager.setOffscreenPageLimit(2);

        advPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //L.d("TestActivity  dispatchTouchEvent" , ev.getAction() + "");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //L.d("TestActivity  onTouchEvent" , ev.getAction() + "");
        return super.onTouchEvent(ev);
    }


    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
            L.d("PagerAdapter  destroyItem " , arg1 + "");
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            L.d("PagerAdapter  instantiateItem " , arg1 + "");
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
}
