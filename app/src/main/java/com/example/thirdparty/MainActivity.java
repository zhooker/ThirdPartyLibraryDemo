package com.example.thirdparty;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.thirdparty.hybrid.JSBridgeActivity;
import com.example.thirdparty.hybrid.JSInterfaceActivity;
import com.example.thirdparty.retrofit.download.RetrofitDownloadActivity;
import com.example.thirdparty.rxjava.RxJavaActivity;
import com.example.thirdparty.rxjava.rxbus.RxBusActivity;
import com.example.thirdparty.rxjava.rxjava2.RxJava2Activity;

public class MainActivity extends AppCompatActivity {

    private static final Class<? extends BaseActivity>[] ACTIVITIES = new Class[]{
            RxJavaActivity.class,
            RxJava2Activity.class,
            JSInterfaceActivity.class,
            JSBridgeActivity.class,
            RetrofitDownloadActivity.class,
            RxBusActivity.class
    };

    private static final String[] ACTIVITIE_DESC = new String[]{
            "RxJava + Retrofit + OKHttp Demo",
            "RxJava Demo : 常用API使用",
            "HyBrid : JSInterface Demo",
            "HyBrid : JSBridge Demo",
            "Retrofit : Retrofit Download Demo",
            "Rxjava : RxBus　Demo"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = (LinearLayout) findViewById(R.id.btn_container);
        for (int i = 0; i < ACTIVITIES.length; i++) {
            Button btn = createButton();
            final Class<? extends Activity> clazz = ACTIVITIES[i];
            final String title = ACTIVITIE_DESC[i];
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToActivity(clazz, title);
                }
            });
            btn.setText((i + 1) + "、" + title);
            container.addView(btn);
        }
    }

    private Button createButton() {
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setPadding(5, 10, 5, 10);
        return btn;
    }

    private void goToActivity(Class<? extends Activity> clazz, String title) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, clazz);
        intent.putExtra(BaseActivity.NAME, title);
        startActivity(intent);
    }
}
