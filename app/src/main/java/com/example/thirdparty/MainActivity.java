package com.example.thirdparty;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.thirdparty.animation.HardwareAnimationActivity;
import com.example.thirdparty.permission.PermissionActivity;
import com.example.thirdparty.retrofit.cache.RetrofitCacheActivity;
import com.example.thirdparty.retrofit.download.RetrofitDownloadActivity;
import com.example.thirdparty.rxjava.RxJavaActivity;
import com.example.thirdparty.rxjava.rxbus.RxBusActivity;
import com.example.thirdparty.rxjava.rxjava2.RxJavaAPIActivity;
import com.example.thirdparty.test.L;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final Class<? extends BaseActivity>[] ACTIVITIES = new Class[]{
            RxJavaAPIActivity.class,
            RxJavaActivity.class,
            RxBusActivity.class,
            RetrofitDownloadActivity.class,
            RetrofitCacheActivity.class,
            PermissionActivity.class,
            HardwareAnimationActivity.class
    };

    private static final String[] ACTIVITIE_DESC = new String[]{
            "RxJava API Demo\nRxJava常用API使用",
            "RxJava + Retrofit + OKHttp Demo\n使用MVP结构,网络下载列表并显示Demo",
            "Rxjava\nRxBus　Demo",
            "Retrofit\n使用Retrofit下载大文件Demo",
            "Retrofit\nRetrofit使用缓存的Demo",
            "RxPermission\n使用RxPermision动态检测权限",
            "硬件加速动画"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = (LinearLayout) findViewById(R.id.btn_container);
        for (int i = 0; i < ACTIVITIES.length; i++) {
            Button btn = createButton();
            final Class<? extends Activity> clazz = ACTIVITIES[i];
            final String title = ACTIVITIE_DESC[i].split("\n")[0];
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToActivity(clazz, title);
                }
            });
            btn.setText((i + 1) + "、" + ACTIVITIE_DESC[i]);
            container.addView(btn);
        }
    }

    private Button createButton() {
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setAllCaps(false);
        btn.setLineSpacing(1.2f, 1.2f);
        btn.setPadding(5, 30, 5, 30);
        return btn;
    }

    public void goToActivity(Class<? extends Activity> clazz, String title) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, clazz);
        intent.putExtra(BaseActivity.NAME, title);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d("onStop",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d("onDestroy",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d("onPause",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d("onResume",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.d("onStart",getLocalClassName() + ",isTaskRoot=" + isTaskRoot() + ",isTopOfTask=" + isTopOfTask());
    }

    private boolean isTopOfTask() {
        try {
            Class clazz = getClass();
            while (!clazz.equals(Activity.class)) {
                clazz = clazz.getSuperclass();
            }

            Method method = clazz.getDeclaredMethod("isTopOfTask",new Class[] {});
            method.setAccessible(true);
            return (boolean) method.invoke(this, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
            L.d("isTopOfTask","isTopOfTask  error = " + e.getMessage());
        }
        return false;
    }
}
