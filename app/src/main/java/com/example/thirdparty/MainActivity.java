package com.example.thirdparty;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.thirdparty.rxjava.flowable.FlowableActivity;
import com.example.thirdparty.rxjava.rxbus.RxBusActivity;
import com.example.thirdparty.rxjava.rxjava2.RxJavaAPIActivity;

public class MainActivity extends AppCompatActivity {

    private static final Class<? extends BaseActivity>[] ACTIVITIES = new Class[]{
            RxJavaAPIActivity.class,
            RxBusActivity.class,
            FlowableActivity.class
    };

    private static final String[] ACTIVITIE_DESC = new String[]{
            "RxJava2 API Demo\nRxJava2常用API使用",
            "Rxjava2\n RxBus　Demo",
            "Rxjava2 \nFlowable　Demo"
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
