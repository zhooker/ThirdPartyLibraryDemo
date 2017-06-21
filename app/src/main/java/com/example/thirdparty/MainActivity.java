package com.example.thirdparty;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.thirdparty.hybrid.JSBridgeActivity;
import com.example.thirdparty.hybrid.JSInterfaceActivity;
import com.example.thirdparty.permission.PermissionActivity;
import com.example.thirdparty.retrofit.download.RetrofitDownloadActivity;
import com.example.thirdparty.rxjava.RxJavaActivity;
import com.example.thirdparty.rxjava.rxbus.RxBusActivity;
import com.example.thirdparty.rxjava.rxjava2.RxJavaAPIActivity;

public class MainActivity extends AppCompatActivity {

    private static final Class<? extends BaseActivity>[] ACTIVITIES = new Class[]{
            RxJavaAPIActivity.class,
            RxJavaActivity.class,
            RxBusActivity.class,
            RetrofitDownloadActivity.class,
            JSInterfaceActivity.class,
            JSBridgeActivity.class,
            PermissionActivity.class
    };

    private static final String[] ACTIVITIE_DESC = new String[]{
            "RxJava API Demo\nRxJava常用API使用",
            "RxJava + Retrofit + OKHttp Demo\n使用MVP结构,网络下载列表并显示Demo",
            "Rxjava\nRxBus　Demo",
            "Retrofit\n使用Retrofit下载大文件Demo",
            "HyBrid\nHyBrid　JSInterface Demo",
            "HyBrid\nHyBrid　JSBridge Demo",
            "RxPermission\n使用RxPermision动态检测权限"
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

    private void goToActivity(Class<? extends Activity> clazz, String title) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, clazz);
        intent.putExtra(BaseActivity.NAME, title);
        startActivity(intent);
    }
}
